package com.medcard.controllers;

import com.medcard.dto.*;
import com.medcard.entities.Doctor;
import com.medcard.entities.Form;
import com.medcard.entities.Patient;
import com.medcard.mapper.*;
import com.medcard.repositories.DoctorRepository;
import com.medcard.services.*;
import com.medcard.services.impl.DoctorServiceImpl;
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
@RequestMapping("/doctor")
@AllArgsConstructor

public class DoctorController {

	private final DoctorServiceImpl doctorService;
	private final PatientService patientService;
	private final FormService formService;
	private final AppointmentService appointmentService;
	private final DoctorMapper doctorMapper;
	private final DoctorRepository doctorRepository;

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
	public String doctorProfile(Model model, Principal principal) {
		String doctorUsername = principal.getName();
		Doctor doctor = doctorRepository.findByUserEmail(doctorUsername);
		DoctorDto doctorDto = doctorMapper.convertToDto(doctorService.getById(doctor.getId()));
		model.addAttribute("doctor", doctorDto);
		return "doctor/doctor-profile";
	}

	@GetMapping("/update/profile")
	public String doctorProfileUpdate(Model model, Principal principal) {
		String doctorUsername = principal.getName();
		Doctor doctor = doctorRepository.findByUserEmail(doctorUsername);
		DoctorDto doctorDto = doctorMapper.convertToDto(doctorService.getById(doctor.getId()));
		model.addAttribute("doctor", doctorDto);
		return "doctor/doctor-update-profile";
	}

	@PostMapping("/update")
	public String update(@ModelAttribute DoctorUpdateRequest updateRequest, Model model,
						 @RequestParam("profileImg") MultipartFile file,
						 @RequestParam("imgName")String imgName, Principal principal) throws IOException {
		String doctorUsername = principal.getName();
		Doctor doctor = doctorRepository.findByUserEmail(doctorUsername);

		String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/IMAGES";

		String imageUUID;
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		} else {
			imageUUID = imgName;
		}
		doctorService.update(doctor.getId(), updateRequest, imageUUID);
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
	public String appointmentsByDoctorId(Model model, Principal principal) {
		String doctorUsername = principal.getName();
		Doctor doctor = doctorRepository.findByUserEmail(doctorUsername);
		model.addAttribute("appointments", appointmentService.getAppointmentsByDoctor(doctor.getId()));
		return "doctor/appointments";
	}

	@GetMapping(value = "/delete/appointment/{id}")
	public String delete(@PathVariable Long id) {
		appointmentService.deleteForDoctor(id);
		return "redirect:/doctor";
	}
}
