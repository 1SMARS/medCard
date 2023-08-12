package com.medcard.services;

import com.medcard.dto.PatientUpdateRequest;
import com.medcard.entities.Patient;

import java.util.List;

public interface PatientService {

//    Patient save(RegisterPatientRequest registerPatientRequest);

    Patient getById(Long id);

    Patient update(Long id, PatientUpdateRequest patientUpdateRequest);

    List<Patient> getAll();

    void delete(Long id);
}
