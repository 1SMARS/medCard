package com.medcard.services;

import com.medcard.dto.doctor.DoctorRequest;
import com.medcard.dto.doctor.DoctorRequests;
import com.medcard.dto.doctor.DoctorResponse;
import com.medcard.dto.doctor.DoctorResponses;

import java.util.List;

public interface DoctorService {

     DoctorResponse save(DoctorRequest doctorRequest);

     void deleteById(Long id);

     DoctorResponses update(Long id, DoctorRequests doctorRequests);

     DoctorResponses getById(Long id);

     List<DoctorResponses> getAll();
}
