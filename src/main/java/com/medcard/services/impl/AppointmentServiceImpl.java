package com.medcard.services.impl;

import com.medcard.dto.appointment.AppointmentRequest;
import com.medcard.dto.appointment.AppointmentResponse;
import com.medcard.dto.patient.PatientResponses;
import com.medcard.entities.Appointment;
import com.medcard.entities.Doctor;
import com.medcard.entities.History;
import com.medcard.entities.Patient;
import com.medcard.repositories.*;
import com.medcard.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientServiceImpl patientService;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final FormRepository formRepository;
    private final DoctorServiceImpl doctorService;
    private final HistoryRepository historyRepository;


    @Autowired
    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, PatientServiceImpl patientService, DoctorRepository doctorRepository, PatientRepository patientRepository, FormRepository formRepository, DoctorServiceImpl doctorService, HistoryRepository historyRepository) {
        this.appointmentRepository = appointmentRepository;
        this.patientService = patientService;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.formRepository = formRepository;
        this.doctorService = doctorService;
        this.historyRepository = historyRepository;
    }

    @Override
    public AppointmentResponse save(Long doctorId, Long patientId, String appointmentTime) {
        Appointment appointment = new Appointment();

        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NoSuchElementException("doctor with id: " + doctorId + " is not exist!"));
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new NoSuchElementException("patient with id: " + patientId + " is not exist!"));

        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        doctor.addAppointment(appointment);
        patient.setDoctor(doctor);
        doctor.addPatient(patient);
        appointment.setAppointmentTime(appointmentTime);

        History history = new History();
        history.setAppointmentTime(appointment.getAppointmentTime());
        history.setDoctorName(doctor.getUser().getFirstName());
        history.setDoctorSurname(doctor.getUser().getLastName());
        history.setSpecialization(doctor.getSpecialization());
        historyRepository.save(history);
        appointmentRepository.save(appointment);

        return new AppointmentResponse(appointment.getId(), appointment.getAppointmentTime(), patientService.convertToResponses(appointment.getPatient()));
    }

    @Override
    public AppointmentResponse update(Long id, AppointmentRequest appointmentRequest) {
        return null;
    }

    @Override
    public void deleteForDoctor(Long id) {
        appointmentRepository.deleteById(id);
    }


    @Override
    public List<AppointmentResponse> getAll() {
        Iterable<Appointment> appointments = appointmentRepository.findAll();
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();

        for (Appointment appointment : appointments) {
            AppointmentResponse appointmentResponse = new AppointmentResponse(
                    appointment.getId(),
                    appointment.getAppointmentTime(),
                    patientService.convertToResponses(appointment.getPatient())
            );
            appointmentResponses.add(appointmentResponse);
        }

        return appointmentResponses;
    }

    @Override
    public AppointmentResponse getById(Long id) {
        return convertToResponse(appointmentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("appointment with id: " + id + " is not exist!")));
    }

    @Override
    public List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new NoSuchElementException("Doctor with id: " + doctorId + " does not exist!"));
        List<Appointment> appointments = appointmentRepository.findByDoctor(doctor);

        List<AppointmentResponse> appointmentResponses = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentResponse appointmentResponse = new AppointmentResponse(
                    appointment.getId(),
                    appointment.getAppointmentTime(),
                    patientService.convertToResponses(appointment.getPatient())
            );
            appointmentResponses.add(appointmentResponse);
        }

        return appointmentResponses;
    }

    private AppointmentResponse convertToResponse(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        AppointmentResponse appointmentResponse = new AppointmentResponse();
        if (appointment.getId() != null) {
            appointmentResponse.setId(appointment.getId());
        }
        appointmentResponse.setAppointmentTime(appointment.getAppointmentTime());

        PatientResponses patientResponses = new PatientResponses();
        if (appointment.getPatient().getId() != null) {
            patientResponses.setId(appointment.getPatient().getId());
        }
        patientResponses.setEmail(appointment.getPatient().getUser().getEmail());
        patientResponses.setFirstName(appointment.getPatient().getUser().getFirstName());
        patientResponses.setLastName(appointment.getPatient().getUser().getLastName());
        patientResponses.setCity(appointment.getPatient().getUser().getCity());
        patientResponses.setPhoneNumber(appointment.getPatient().getUser().getPhoneNumber());
        patientResponses.setProfileImg(appointment.getPatient().getUser().getProfileImg());
        patientResponses.setBirthDate(appointment.getPatient().getUser().getBirthDate());

        appointmentResponse.setPatientResponses(patientResponses);

        return appointmentResponse;
    }
}
