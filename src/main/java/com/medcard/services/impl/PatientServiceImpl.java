package com.medcard.services.impl;

import com.medcard.dto.patient.PatientRequest;
import com.medcard.dto.patient.PatientRequests;
import com.medcard.dto.patient.PatientResponse;
import com.medcard.dto.patient.PatientResponses;
import com.medcard.entities.*;
import com.medcard.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medcard.repositories.PatientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class PatientServiceImpl implements PatientService {


	private final PatientRepository patientRepository;

	@Autowired
	public PatientServiceImpl(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}

	@Override
	public List<PatientResponses> getAll() {

		Iterable<Patient> patientsIterable = patientRepository.findAll();
		List<Patient> patients = new ArrayList<>();

		for (Patient patient : patientsIterable) {
			patients.add(patient);
		}

		return patients.stream().map(this::convertToResponses).collect(Collectors.toList());
	}

	@Override
	public PatientResponse save(PatientRequest patientRequest) {
		Patient patient = new Patient();
		User user = new User();

		patient.setUser(user);
		user.setPatient(patient);

		patient.getUser().setFirstName(patientRequest.getFirstname());
		patient.getUser().setLastName(patientRequest.getLastname());
		patient.getUser().setEmail(patientRequest.getEmail());
		patient.getUser().setPassword(patientRequest.getPassword());

		patientRepository.save(patient);

		return new PatientResponse(patient.getId(), patient.getUser().getFirstName());
	}

	@Override
	public PatientResponses update(Long id, PatientRequests patientRequests) {
		Patient patient1 = patientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient with id: " + id + " is not exist!"));

		update(patient1, patientRequests);

		patientRepository.save(patient1);

		return new PatientResponses(patient1.getId(), patient1.getUser().getFirstName(), patient1.getUser().getLastName(),
				patient1.getUser().getBirthDate(), patient1.getUser().getCity(), patient1.getUser().getEmail(),
				patient1.getUser().getPhoneNumber(), patient1.getUser().getProfileImg());
	}


	private void update(Patient patient, PatientRequests patientRequests) {
		if (patientRequests.getFirstName() != null) {
			patient.getUser().setFirstName(patientRequests.getFirstName());
		}
		if (patientRequests.getLastName() != null) {
			patient.getUser().setLastName(patientRequests.getLastName());
		}
		if (patientRequests.getCity() != null) {
			patient.getUser().setCity(patientRequests.getCity());
		}
		if (patientRequests.getBirthDate() != null) {
			patient.getUser().setBirthDate(patientRequests.getBirthDate());
		}
		if (patientRequests.getPhoneNumber() != null) {
			patient.getUser().setPhoneNumber(patientRequests.getPhoneNumber());
		}
		if (patientRequests.getProfileImg() != null) {
			patient.getUser().setProfileImg(patientRequests.getProfileImg());
		}

		if (patientRequests.getEmail() != null) {
			patient.getUser().setEmail(patientRequests.getEmail());
		}
	}


	@Override
	public PatientResponses getById(Long id) {
		return convertToResponses(patientRepository.findById(id).orElseThrow(() -> new NoSuchElementException("patient with id: " + id + " is not exist!")));
	}

	@Override
	public void delete(Long id) {
		patientRepository.deleteById(id);
	}

	public PatientResponses convertToResponses(Patient patient) {
		if (patient == null) {
			return null;
		}
		PatientResponses responses = new PatientResponses();
		if (patient.getId() != null) {
			responses.setId(patient.getId());
		}
		responses.setCity(patient.getUser().getCity());
		responses.setBirthDate(patient.getUser().getBirthDate());
		responses.setEmail(patient.getUser().getEmail());
		responses.setFirstName(patient.getUser().getFirstName());
		responses.setPhoneNumber(patient.getUser().getPhoneNumber());
		responses.setLastName(patient.getUser().getLastName());
		responses.setProfileImg(patient.getUser().getProfileImg());

		return responses;
	}
}
