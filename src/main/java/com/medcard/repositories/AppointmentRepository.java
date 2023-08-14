package com.medcard.repositories;

import com.medcard.entities.Appointment;
import com.medcard.entities.Doctor;
import com.medcard.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Long> {

    List<Appointment> findByDoctorId(Long doctorId);
    List<Appointment> findByDoctorIdAndAppointmentTime(Long doctorId, String time);
}
