package com.medcard.repositories;

import com.medcard.entities.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FormRepository extends JpaRepository<Form, Long> {
    Form findByPatientIdAndDoctorId(Long patientId, Long doctorId);
}
