package com.medcard.services.impl;

import com.medcard.entities.*;
import com.medcard.repositories.*;
import com.medcard.services.AppointmentService;
import com.medcard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

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

        List<Appointment> appointments = new ArrayList<>();
        List<Patient> patients = new ArrayList<>();

        appointments.add(appointment);
        patients.add(patient);

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        doctor.setAppointments(appointments);
        patient.setDoctor(doctor);

        doctor.setPatients(patients);
        appointment.setAppointmentTime(appointment.getAppointmentTime());

        History history = new History();
        history.setPatient(patient);
        history.setAppointmentTime(appointment.getAppointmentTime().toString());
        history.setDoctorName(doctor.getUser().getFirstname());
        history.setDoctorSurname(doctor.getUser().getLastname());
        history.setSpecialization(doctor.getSpecialization());

        patient.setHistory(history);
        historyRepository.save(history);

        return appointmentRepository.save(appointment);
    }

//    @Override
//    public Appointment update(Long id, Appointment updateAppointment) {
//        Appointment appointment = getById(id);
//        appointment.setAppointmentTime(updateAppointment.getAppointmentTime());
//
//    }

    @Override
    public void deleteForDoctor(Long id) {
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

    public boolean isTimeAvailable(String selectedTime) {
        List<Appointment> existingAppointments = (List<Appointment>) appointmentRepository.findAll();

        for (Appointment appointment : existingAppointments) {
            if (appointment.getAppointmentTime().equals(selectedTime)) {
                return false;
            }
        }
        return true;
    }

}


