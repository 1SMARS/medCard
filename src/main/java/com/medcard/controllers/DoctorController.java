package com.medcard.controllers;

import com.medcard.dto.*;
import com.medcard.entities.Form;
import com.medcard.entities.Patient;
import com.medcard.mapper.*;
import com.medcard.security.CurrentDoctorFinder;
import com.medcard.services.*;
import com.medcard.services.impl.DoctorServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/doctor")
@AllArgsConstructor

public class DoctorController {

	private final DoctorServiceImpl doctorService;
	private final PatientService patientService;
	private final FormService formService;
	private final AppointmentService appointmentService;
	private final DoctorMapper doctorMapper;
	private final CurrentDoctorFinder currentUserFinder;

	@GetMapping
	public String home() {
		return "doctor/doctor-home";
	}

	@GetMapping("/help")
	public String help() {
		return "doctor/help";
	}

	@GetMapping("/doctors")
	public String doctors(Model model) {
		List<DoctorDto> doctors = doctorMapper.convertToDtoList(doctorService.getAll());
		model.addAttribute("doctors", doctors);
		return "doctor/doctors";
	}

	@GetMapping("/profile")
	public String doctorProfile(Model model) {
		long doctorId = currentUserFinder.getCurrentUserId();
		DoctorDto doctorDto = doctorMapper.convertToDto(doctorService.getById(doctorId));
		model.addAttribute("doctor", doctorDto);
		return "doctor/doctor-profile";
	}

	@GetMapping("/update/profile")
	public String doctorProfileUpdate(Model model) {
		long doctorId = currentUserFinder.getCurrentUserId();
		DoctorDto doctorDto = doctorMapper.convertToDto(doctorService.getById(doctorId));
		model.addAttribute("doctor", doctorDto);
		return "doctor/doctor-update-profile";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute DoctorUpdateRequest doctor, Model model) {
		long id = currentUserFinder.getCurrentUserId();
		doctorService.update(id, doctor);
		return "redirect:/doctor/profile";
	}

	@GetMapping("/form/{patientId}")
	public String showForm(@PathVariable Long patientId, Model model) {
		Patient patient = patientService.getById(patientId);
		Form existingForm = formService.getFormByPatientId(patientId);
		if (existingForm != null) {
			model.addAttribute("form", existingForm);
		} else {
			model.addAttribute("form", new Form());
		}

		model.addAttribute("patient", patient);
		return "doctor/fill-form";
	}

	@PostMapping(value = "/fill/form/{patientId}")
	public String fillForm(@PathVariable Long patientId, @ModelAttribute Form form) {
		Form existingForm = formService.getFormByPatientId(patientId);
		if (existingForm != null) {
			existingForm.setDescription(form.getDescription());
			existingForm.setDisease(form.getDisease());
			formService.updateForm(existingForm.getId(), existingForm);
		} else {
			formService.createForm(patientId, form);
		}

		return "redirect:/doctor";
	}


	@GetMapping(value = "/appointments")
	public String appointmentsByDoctorId(Model model) {
		Long doctorId = currentUserFinder.getCurrentUserId();
		model.addAttribute("appointments", appointmentService.getAppointmentsByDoctor(doctorId));
		return "doctor/appointments";
	}

	@GetMapping(value = "/delete/appointment/{id}")
	public String delete(@PathVariable Long id) {
		appointmentService.deleteForDoctor(id);
		return "redirect:/doctor";
	}
}
