package com.medcard.services.impl;

import com.medcard.dto.PatientUpdateRequest;
import com.medcard.entities.*;
import com.medcard.services.PatientService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.medcard.repositories.PatientRepository;
import java.util.List;

@Service
@AllArgsConstructor
public class PatientServiceImpl implements PatientService {

	private final PatientRepository patientRepository;

	@Override
	public List<Patient> getAll() {
		return (List<Patient>) patientRepository.findAll();
	}

	@Override
	public Patient update(Long id, PatientUpdateRequest updatePatient, String imageUUID) {
			Patient patient = getById(id);
			patient.getUser().setFirstname(updatePatient.getFirstname());
			patient.getUser().setLastname(updatePatient.getLastname());
			patient.getUser().setCity(updatePatient.getCity());
			patient.getUser().setBirthDate(updatePatient.getBirthDate());
			patient.getUser().setPhoneNumber(updatePatient.getPhoneNumber());
			patient.getUser().setProfileImg(imageUUID);

			return patientRepository.save(patient);
	}

	@Override
	public Patient getById(Long id) {
		return patientRepository.findById(id).orElseThrow();
	}

	@Override
	public void delete(Long id) {
		patientRepository.deleteById(id);
	}
}
