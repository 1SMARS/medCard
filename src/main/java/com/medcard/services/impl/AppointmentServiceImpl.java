package com.medcard.services.impl;

import com.medcard.entities.*;
import com.medcard.repositories.*;
import com.medcard.services.AppointmentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@AllArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final HistoryRepository historyRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public Appointment save(Long doctorId, Long patientId, Appointment appointment) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        Patient patient = patientRepository.findById(patientId).orElseThrow();

        // Check if the patient already has an appointment with the given doctor
        boolean hasAppointment = hasAppointmentWithDoctor(patientId, doctorId);

        if (!hasAppointment) {
            // Save the appointment
            appointment.setDoctor(doctor);
            appointment.setPatient(patient);
            appointmentRepository.save(appointment);

            // Update associations
            patient.getAppointments().add(appointment);
            doctor.getAppointments().add(appointment);

            createHistoryRecord(patient, doctor, appointment);
            return appointment;

        } else {
            // Handle case when patient already has an appointment with the doctor
            throw new IllegalArgumentException("Patient already has an appointment with this doctor.");
        }
    }

    // Call this method after the appointment is successfully saved
    private void createHistoryRecord(Patient patient, Doctor doctor, Appointment savedAppointment) {
        History history = new History();
        history.setPatient(patient);
        history.setAppointmentTime(savedAppointment.getAppointmentTime().toString());
        history.setDoctorName(doctor.getUser().getFirstname());
        history.setDoctorSurname(doctor.getUser().getLastname());
        history.setSpecialization(doctor.getSpecialization());

        List<History> histories = doctor.getHistories(); // Manage the relationship from the Doctor side
        if (histories == null) {
            histories = new ArrayList<>();
        }
        histories.add(history);
        doctor.setHistories(histories);

        history.setDoctor(doctor);

        historyRepository.save(history);
    }

    @Override
    public boolean isTimeAvailable(String time, Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTime(doctorId, time);
        return appointments.isEmpty();
    }

    @Override
    public void deleteForDoctor(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();

        Doctor doctor = appointment.getDoctor();
        doctor.getAppointments().remove(appointment);
        doctorRepository.save(doctor);

        Patient patient = appointment.getPatient();
        if (patient != null) {
            patient.getAppointments().remove(appointment);
            patientRepository.save(patient);
        }

        appointmentRepository.deleteById(id);
    }


    @Override
    public List<Appointment> getAll() {
        return (List<Appointment>) appointmentRepository.findAll();
    }

    @Override
    public Appointment getById(Long id) {
        return appointmentRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Appointment> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow();
        return appointmentRepository.findByDoctorId(doctor.getId());
    }

    @Override
    public List<String> generateAppointmentTimes() {
        return Arrays.asList("10:00", "11:30", "12:40", "13:20", "14:50", "15:40", "16:20");
    }

    @Override
    public boolean hasAppointmentWithDoctor(Long patientId, Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByPatientIdAndDoctorId(patientId, doctorId);
        return !appointments.isEmpty();
    }
}


