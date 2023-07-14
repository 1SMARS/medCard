package com.medcard.services;

import com.medcard.dto.patient.PatientRequest;
import com.medcard.dto.patient.PatientRequests;
import com.medcard.dto.patient.PatientResponse;
import com.medcard.dto.patient.PatientResponses;
import java.util.List;

public interface PatientService {

    PatientResponse save(PatientRequest patientRequest);

    PatientResponses getById(Long id);

    PatientResponses update(Long id, PatientRequests patientRequests);

    List<PatientResponses> getAll();

    void delete(Long id);
}
