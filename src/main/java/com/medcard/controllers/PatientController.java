package com.medcard.controllers;

import com.medcard.dto.*;
import com.medcard.entities.Appointment;
import com.medcard.entities.Patient;
import com.medcard.mapper.DoctorMapper;
import com.medcard.mapper.FormMapper;
import com.medcard.mapper.PatientMapper;
import com.medcard.repositories.UserRepository;
import com.medcard.security.CurrentUserFinder;
import com.medcard.services.UserService;
import com.medcard.services.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    private final FormMapper formMapper;
    private final DoctorMapper doctorMapper;
    private final CurrentUserFinder currentUserFinder;
    private final UserService userService;
    private final UserRepository userRepository;


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
    public String showProfile(Model model) {
        long id = currentUserFinder.getCurrentUserId();
        PatientDto patientDto = patientMapper.apply(patientServiceImpl.getById(id));
        model.addAttribute("patient", patientDto);
        return "client/client-profile";
    }

    @GetMapping("/profile/edit")
    public String showUpdateProfile(Model model) {
        long id = currentUserFinder.getCurrentUserId();
        PatientDto patientDto = patientMapper.apply(patientServiceImpl.getById(id));
        model.addAttribute("patient", patientDto);
        return "client/client-update-profile";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute PatientUpdateRequest patient, Model model) {
        long id = currentUserFinder.getCurrentUserId();
        patientServiceImpl.update(id, patient);
        return "redirect:/patient/profile";
    }

    @GetMapping("/get/form")
    public String getFormByPatientId(Model model) {
        Long patientId = currentUserFinder.getCurrentUserId();

        if (formService.getFormByPatientId(patientId) == null) {
            return "client/client-empty-form";
        }
        model.addAttribute("form", formService.getFormByPatientId(patientId));
        return "client/client-form";
    }

    @GetMapping("/appointment/{doctorId}")
    public String showAppointmentForm(Model model, @PathVariable Long doctorId) {
        DoctorDto doctorDto = doctorMapper.convertToDto(doctorServiceImpl.getById(doctorId));
        long patientId = currentUserFinder.getCurrentUserId();
        Patient patientDto = patientServiceImpl.getById(patientId);
        List<String> availableTimes = appointmentService.generateAppointmentTimes();
        model.addAttribute("availableTimes", availableTimes);
        model.addAttribute("doctor", doctorDto);
        model.addAttribute("patient", patientDto);
        model.addAttribute("appointmentService", appointmentService);
        model.addAttribute("appointment", new AppointmentDto()); // Add an empty Appointment object for form binding
        return "client/client-enroll";
    }

    @PostMapping("/create/appointment/{doctorId}")
    public String createAppointment(@PathVariable Long doctorId, Model model, Appointment appointment) {
        Long patientId = currentUserFinder.getCurrentUserId();
        appointmentService.save(doctorId, patientId, appointment);
        return "redirect:/patient";
    }

    @GetMapping("/histories")
    public String showAllHistories(Model model) {
        Long patientId = currentUserFinder.getCurrentUserId();
        model.addAttribute("histories", historyService.getAll(patientId));
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
