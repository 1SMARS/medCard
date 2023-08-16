package com.medcard.repositories;

import com.medcard.entities.Doctor;
import com.medcard.entities.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> getAllByPatientId(Long patientId);
    void deleteByDoctor(Doctor doctor);
}
