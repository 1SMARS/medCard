package com.medcard.services;

import com.medcard.dto.DoctorCreateRequest;
import com.medcard.dto.DoctorDto;
import com.medcard.dto.DoctorUpdateRequest;
import com.medcard.entities.Doctor;

import java.util.List;

public interface DoctorService {

     void saveDoctor(DoctorCreateRequest createRequest);

     void deleteById(Long id);

     Doctor update(Long id, DoctorUpdateRequest doctorUpdateRequest);

     Doctor getById(Long id);

     List<Doctor> getAll();
}
