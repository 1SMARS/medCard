package com.medcard.controllers;

import com.medcard.dto.*;
import com.medcard.entities.Appointment;
import com.medcard.entities.Doctor;
import com.medcard.entities.Form;
import com.medcard.entities.Patient;
import com.medcard.mapper.DoctorMapper;
import com.medcard.mapper.PatientMapper;
import com.medcard.repositories.DoctorRepository;
import com.medcard.repositories.PatientRepository;
import com.medcard.services.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;
    private final DoctorService doctorService;
    private final FormService formService;
    private final AppointmentService appointmentService;
    private final HistoryService historyService;
    private final PatientMapper patientMapper;
    private final DoctorMapper doctorMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @GetMapping
    public String home() {
        return "client/client-home";
    }

    @GetMapping("/doctors")
    public String doctors(Model model) {
        List<DoctorDto> doctors = doctorMapper.convertToDtoList(doctorService.getAll());
        model.addAttribute("doctors", doctors);
        return "client/client-doctors";
    }

    @GetMapping("/doctor/{id}")
    public String showDoctorDetails(@PathVariable Long id, Model model) {
        DoctorDto doctorDto =  doctorMapper.convertToDto(doctorService.getById(id));
        model.addAttribute("doctor", doctorDto);
        return "client/doctor-details";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);
        PatientDto patientDto = patientMapper.apply(patientService.getById(patient.getId()));
        model.addAttribute("patient", patientDto);
        return "client/client-profile";
    }

    @GetMapping("/profile/edit")
    public String showUpdateProfile(Model model, Principal principal) {
        String patientUsername = principal.getName();
        Patient patient = patientRepository.findByUserEmail(patientUsername);
        PatientDto patientDto = patientMapper.apply(patientService.getById(patient.getId()));
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

        patientService.update(patient.getId(), updateRequest, imageUUID);
        return "redirect:/patient/profile";
    }

    @GetMapping("/get/form/{doctorId}")
    public String getFormByPatientId(@PathVariable Long doctorId, Model model, Principal principal) {
        String username = principal.getName();
        Patient patient = patientRepository.findByUserEmail(username);
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        if (formService.getFormByPatientId(patient.getId(), doctor.getId()) == null) {
            return "client/client-empty-form";
        }
        model.addAttribute("doctor", doctor);
        model.addAttribute("form", formService.getFormByPatientId(patient.getId(), doctor.getId()));
        return "client/client-form";
    }

    @GetMapping("/appointment/{doctorId}")
    public String showAppointmentForm(Model model, @PathVariable Long doctorId, Principal principal) {
        String patientUsername = principal.getName();
        Patient patientDto = patientRepository.findByUserEmail(patientUsername);
        DoctorDto doctorDto = doctorMapper.convertToDto(doctorService.getById(doctorId));
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

        boolean hasAppointment = appointmentService.hasAppointmentWithDoctor(patient.getId(), doctorId);

        if (hasAppointment) {
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
