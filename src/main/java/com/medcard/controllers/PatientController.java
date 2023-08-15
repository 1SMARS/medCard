package com.medcard.controllers;

import com.medcard.dto.*;
import com.medcard.entities.Appointment;
import com.medcard.entities.Patient;
import com.medcard.mapper.DoctorMapper;
import com.medcard.mapper.PatientMapper;
import com.medcard.repositories.PatientRepository;
import com.medcard.services.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.IncorrectResultSetColumnCountException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@AllArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientServiceImpl patientServiceImpl;
    private final DoctorServiceImpl doctorServiceImpl;
    private final FormServiceImpl formService;
    private final AppointmentServiceImpl appointmentService;
    private final HistoryServiceImpl historyService;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final PatientRepository patientRepository;

    @GetMapping
    public String home() {
        return "client/client-home";
    }

    @GetMapping("/doctors")
    public String doctors(Model model) {
        List<DoctorDto> doctors = doctorMapper.convertToDtoList(doctorServiceImpl.getAll());
        model.addAttribute("doctors", doctors);
        return "client/client-doctors";
    }

    @GetMapping("/doctor/{id}")
    public String showDoctorDetails(@PathVariable Long id, Model model) {
        DoctorDto doctorDto =  doctorMapper.convertToDto(doctorServiceImpl.getById(id));
        model.addAttribute("doctor", doctorDto);
        return "client/doctor-details";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);
        PatientDto patientDto = patientMapper.apply(patientServiceImpl.getById(patient.getId()));
        model.addAttribute("patient", patientDto);
        return "client/client-profile";
    }

    @GetMapping("/profile/edit")
    public String showUpdateProfile(Model model, Principal principal) {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);
        PatientDto patientDto = patientMapper.apply(patientServiceImpl.getById(patient.getId()));
        model.addAttribute("patient", patientDto);
        return "client/client-update-profile";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute PatientUpdateRequest updateRequest, Model model,
                         @RequestParam("profileImg")MultipartFile file,
                         @RequestParam("imgName")String imgName, Principal principal) throws IOException {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);

        String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/IMAGES";

        String imageUUID;
        if(!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = imgName;
        }

        patientServiceImpl.update(patient.getId(), updateRequest, imageUUID);
        return "redirect:/patient/profile";
    }

    @GetMapping("/get/form")
    public String getFormByPatientId(Model model, Principal principal) {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);

        if (formService.getFormByPatientId(patient.getId()) == null) {
            return "client/client-empty-form";
        }
        model.addAttribute("form", formService.getFormByPatientId(patient.getId()));
        return "client/client-form";
    }

    @GetMapping("/appointment/{doctorId}")
    public String showAppointmentForm(Model model, @PathVariable Long doctorId, Principal principal) {
        String patientUsername = principal.getName();
        Patient patientDto = patientRepository.findByUserEmail(patientUsername);
        DoctorDto doctorDto = doctorMapper.convertToDto(doctorServiceImpl.getById(doctorId));
        List<String> availableTimes = appointmentService.generateAppointmentTimes();
        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("doctor", doctorDto);
        model.addAttribute("patient", patientDto);
        model.addAttribute("appointmentService", appointmentService);
        model.addAttribute("appointment", new AppointmentDto()); // Add an empty Appointment object for form binding
        return "client/client-enroll";
    }

    @PostMapping("/create/appointment/{doctorId}")
    public String createAppointment(@PathVariable Long doctorId, Model model,
                                    Appointment appointment, Principal principal) {

        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);
        if (patient.getAppointment() != null) {
            return "client/appointmentError";
        }
        appointmentService.save(doctorId, patient.getId(), appointment);
        return "redirect:/patient";
    }

    @GetMapping("/histories")
    public String showAllHistories(Model model, Principal principal) {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);
        model.addAttribute("histories", historyService.getAll(patient.getId()));
        return "client/client-histories";
    }

    @GetMapping("/delete/history/{id}")
    public String deleteHistory(@PathVariable Long id) {
        historyService.delete(id);
        return "redirect:/patient";
    }

    @GetMapping("/help")
    public String help() {
        return "client/help";
    }
}
