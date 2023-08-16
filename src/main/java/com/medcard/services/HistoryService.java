package com.medcard.services;

import com.medcard.entities.Doctor;
import com.medcard.entities.History;

import java.util.List;

public interface HistoryService {

    List<History> getAll(Long patientId);

    void delete(Long id);

    void deleteByDoctor(Doctor doctor);
}
