package com.medcard.services;

import com.medcard.entities.Appointment;

import java.util.List;

public interface AppointmentService {

    Appointment save(Long doctorId, Long patientId, Appointment appointment);

    void deleteForDoctor(Long id);

    List<Appointment> getAll();

    Appointment getById(Long id);

    List<Appointment> getAppointmentsByDoctor(Long doctorId);

//    boolean isTimeBooked(Long doctorId, String appointmentTime);

    List<String> generateAppointmentTimes();

//    void bookAppointment(Long doctorId, String appointmentTime);
}
