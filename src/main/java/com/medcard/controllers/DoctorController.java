package com.medcard.controllers;

import com.medcard.dto.form.FormRequest;
import com.medcard.dto.form.FormResponse;
import com.medcard.dto.appointment.AppointmentResponse;
import com.medcard.dto.doctor.DoctorRequests;
import com.medcard.dto.doctor.DoctorResponses;
import com.medcard.dto.patient.PatientResponses;
import com.medcard.services.impl.AppointmentServiceImpl;
import com.medcard.services.impl.DoctorServiceImpl;
import com.medcard.services.impl.FormServiceImpl;
import com.medcard.services.impl.PatientServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@AllArgsConstructor
@PreAuthorize("hasAnyAuthority('ROLE_DOCTOR')")

public class DoctorController {

	private final DoctorServiceImpl doctorServiceImpl;
	private final PatientServiceImpl patientService;
	private final FormServiceImpl formService;
	private final AppointmentServiceImpl appointmentService;

	@GetMapping(value = "/get/doctors")
	public List<DoctorResponses> doctors() {
		return doctorServiceImpl.getAll();
	}

	@GetMapping(value = "/get/patient/{id}")
	public PatientResponses patient(@PathVariable Long id) {
		return patientService.getById(id);
	}

	@GetMapping(value = "/get/{id}")
	public DoctorResponses doctor(@PathVariable Long id) {
		return doctorServiceImpl.getById(id);
	}

	@PostMapping("/update/{id}")
	public DoctorResponses update(@PathVariable Long id, DoctorRequests doctorRequests) {
		return doctorServiceImpl.update(id, doctorRequests);
	}

	@PostMapping(value = "/fill/patient/form/{patientId}")
	public FormResponse fillForm(@PathVariable Long patientId, FormRequest formRequest) {
		return formService.fillForm(patientId, formRequest);
	}

	@PostMapping(value = "/update/form/{formId}")
	public FormResponse updateForm(@PathVariable Long formId, FormRequest formRequest) {
		return formService.updateForm(formId, formRequest);
	}

	@GetMapping(value = "/get/appointments/{doctorId}")
	public List<AppointmentResponse> appointmentsByDoctorId(@PathVariable Long doctorId) {
		return appointmentService.getAppointmentsByDoctor(doctorId);
	}

	@GetMapping(value = "/delete/appointment/{id}")
	public void delete(@PathVariable Long id) {
		appointmentService.deleteForDoctor(id);
	}
}
