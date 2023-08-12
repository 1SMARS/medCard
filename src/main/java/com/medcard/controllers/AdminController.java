package com.medcard.controllers;
import com.medcard.dto.*;
import com.medcard.entities.Doctor;
import com.medcard.entities.User;
import com.medcard.mapper.DoctorMapper;
import com.medcard.repositories.DoctorRepository;
import com.medcard.repositories.UserRepository;
import com.medcard.security.CurrentUserFinder;
import com.medcard.services.DoctorService;
import com.medcard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor

public class AdminController {

	private final DoctorService doctorServiceImpl;
	private final DoctorRepository doctorRepository;
	private final DoctorMapper doctorMapper;
	private final UserRepository userRepository;
	private final CurrentUserFinder currentUserFinder;
	private final UserService userService;

	@GetMapping
	public String home() {
		return "admin/admin-home";
	}

	@GetMapping("/doctors")
	public String doctors(Model model) {
		List<DoctorDto> doctors = doctorMapper.convertToDtoList(doctorServiceImpl.getAll());
		model.addAttribute("doctors", doctors);
		return "admin/admin-doctors";
	}

	@GetMapping(value="/create/doctor")
	public String register(Model model) {
		model.addAttribute("newAccount", new User());
		model.addAttribute("doctor", new Doctor());
		return "admin/create-doctor";
	}

	@PostMapping("/doctor/save")
	public String saveDoctor(@ModelAttribute("newAccount") DoctorCreateRequest createRequest, BindingResult result, Model model){
		User existingDoctor = userService.findUserByEmail(createRequest.getEmail());

		if(existingDoctor != null && existingDoctor.getEmail() != null && !existingDoctor.getEmail().isEmpty()){
			result.rejectValue("email", null, "There is already an account registered with the same email");
		}

		if(result.hasErrors()){
			model.addAttribute("user", createRequest);
			return "/save/doctor";
		}

		userService.saveUser(createRequest);
		return "redirect:/admin/doctors";
	}

	@GetMapping("/delete/doctor/{id}")
	public String deleteDoctor(@PathVariable Long id) {
		doctorServiceImpl.deleteById(id);
		return "redirect:/admin/doctors";
	}

	@GetMapping("/edit/doctor/{id}")
	public String showUpdateProfile(@PathVariable Long id, Model model) {
		DoctorDto doctorDto = doctorMapper.convertToDto(doctorRepository.findById(id).orElseThrow());
		model.addAttribute("doctor", doctorDto);
		return "admin/update-doctor";
	}

	@PostMapping("/update/doctor/{id}")
	public String updateDoctor(@PathVariable Long id, DoctorUpdateRequest doctor){
		doctorServiceImpl.update(id, doctor);
		return "redirect:/admin/doctors";
	}

	@GetMapping("/profile")
	public String adminProfile(Model model) {
		long id = currentUserFinder.getCurrentUserId();
		User user = userRepository.findById(id).orElseThrow();
		model.addAttribute("admin", user);
		return "admin/admin-profile";
	}

	@GetMapping("/help")
	public String help() {
		return "admin/help";
	}
}