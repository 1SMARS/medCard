package com.medcard.controllers;

import com.medcard.dto.form.FormResponse;
import com.medcard.dto.appointment.AppointmentResponse;
import com.medcard.dto.doctor.DoctorResponses;
import com.medcard.dto.patient.PatientRequest;
import com.medcard.dto.patient.PatientRequests;
import com.medcard.dto.patient.PatientResponse;
import com.medcard.dto.patient.PatientResponses;
import com.medcard.entities.History;
import com.medcard.services.impl.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/patient")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_PATIENT')")
public class PatientController {

	private final PatientServiceImpl patientServiceImpl;
	private final DoctorServiceImpl doctorServiceImpl;
	private final FormServiceImpl formService;
	private final AppointmentServiceImpl appointmentService;
	private final HistoryServiceImpl historyService;

	@GetMapping(value = "/get/doctors")
	public List<DoctorResponses> doctors() {
		return doctorServiceImpl.getAll();
	}

	@GetMapping("/get/doctor/{id}")
	public DoctorResponses showDoctorDetails(@PathVariable Long id) {
		return doctorServiceImpl.getById(id);
	}

	@PostMapping("/create")
	public PatientResponse create(PatientRequest patientRequest) {
		return patientServiceImpl.save(patientRequest);
	}

	@PutMapping("/update/{id}")
	public PatientResponses update(@PathVariable Long id, PatientRequests patientRequests) {
		return patientServiceImpl.update(id, patientRequests);
	}

	@GetMapping("/get/{id}")
	public PatientResponses profile(@PathVariable Long id) {
		return patientServiceImpl.getById(id);
	}


	@GetMapping("/get/form/{patientId}")
	public FormResponse getFormByPatientId(@PathVariable Long patientId) {
		return formService.getFormByPatientId(patientId);
	}

	@PostMapping("/create/appointment/{doctorId}")
	public AppointmentResponse createAppointment(@PathVariable Long doctorId, @RequestParam String appointmentTime, @RequestParam Long patientId) {
		return appointmentService.save(doctorId, patientId, appointmentTime);
	}

	@GetMapping("/get/histories")
	public List<History> getAll() {
		return historyService.getAll();
	}

	@GetMapping("/delete/history/{id}")
	public void deleteHistory(@PathVariable Long id) {
		historyService.delete(id);
	}
}
