package com.medcard.controllers;
import java.util.List;

import com.medcard.dto.doctor.DoctorRequest;
import com.medcard.dto.doctor.DoctorRequests;
import com.medcard.dto.doctor.DoctorResponse;
import com.medcard.dto.doctor.DoctorResponses;
import com.medcard.services.impl.PatientServiceImpl;
import com.medcard.services.impl.DoctorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/admin")
public class AdminController {

	@Autowired
	DoctorServiceImpl doctorServiceImpl;

	@Autowired
	PatientServiceImpl patientServiceImpl;

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

	@PostMapping("/update/doctor/{id}")
	public DoctorResponses updateDoctor(@PathVariable("id") Long id, DoctorRequests doctorRequests){
		return doctorServiceImpl.update(id, doctorRequests);
	}


}
