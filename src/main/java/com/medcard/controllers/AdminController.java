package com.medcard.controllers;

import com.medcard.dto.doctor.DoctorRequest;
import com.medcard.dto.doctor.DoctorRequests;
import com.medcard.dto.doctor.DoctorResponse;
import com.medcard.dto.doctor.DoctorResponses;
import com.medcard.services.impl.DoctorServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")

public class AdminController {

	private final DoctorServiceImpl doctorServiceImpl;


	@GetMapping("/doctors")
	public List<DoctorResponses> doctors() {
		return doctorServiceImpl.getAll();
	}

	@PostMapping(value="/create/doctor")
	public DoctorResponse createDoctor(DoctorRequest doctorRequest) {
		return doctorServiceImpl.save(doctorRequest);
	}

	@GetMapping("/delete/doctor/{id}")
	public void deleteDoctor(@PathVariable Long id) {
         doctorServiceImpl.deleteById(id);
	}

//	@GetMapping("/get/{id}")
//	public AdminResponse admin(@PathVariable Long id) {
//		return adminService.getById(id);
//	}



	@PostMapping("/update/doctor/{id}")
	public DoctorResponses updateDoctor(@PathVariable("id") Long id, DoctorRequests doctorRequests){
		return doctorServiceImpl.update(id, doctorRequests);
	}
}
