package com.medcard.services.impl;

import com.medcard.entities.*;
import com.medcard.repositories.*;
import com.medcard.services.AppointmentService;
import com.medcard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
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

        // Save the appointment first
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Update associations
        List<Appointment> appointments = new ArrayList<>();
        List<Patient> patients = new ArrayList<>();
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(doctor);

        appointments.add(savedAppointment);
        patients.add(patient);

        savedAppointment.setPatient(patient);
        savedAppointment.setDoctor(doctor);

        doctor.setAppointments(appointments);
        patient.setDoctors(doctors);
        patient.setAppointment(savedAppointment);

        doctor.setPatients(patients);
        savedAppointment.setAppointmentTime(appointment.getAppointmentTime());

        History history = new History();
        history.setPatient(patient);
        history.setAppointmentTime(savedAppointment.getAppointmentTime().toString());
        history.setDoctorName(doctor.getUser().getFirstname());
        history.setDoctorSurname(doctor.getUser().getLastname());
        history.setSpecialization(doctor.getSpecialization());

        patient.setHistory(history);
        historyRepository.save(history);

        return savedAppointment;
    }

    public boolean isTimeAvailable(String time, Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorIdAndAppointmentTime(doctorId, time);
        return appointments.isEmpty();
    }

    @Override
    public void deleteForDoctor(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElseThrow();

        // Remove the appointment from the doctor's appointments list
        Doctor doctor = appointment.getDoctor();
        doctor.getAppointments().remove(appointment);
        doctorRepository.save(doctor);

        // Remove the appointment from the patient's appointment
        Patient patient = appointment.getPatient();
        if (patient != null) {
            patient.setAppointment(null);
            patientRepository.save(patient);
        }

        // Delete the appointment
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

    public List<String> generateAppointmentTimes() {
        return Arrays.asList("10:00", "11:30", "12:40", "13:20", "14:50", "15:40", "16:20");
    }
}


