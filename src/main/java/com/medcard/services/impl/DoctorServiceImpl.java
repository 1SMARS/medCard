package com.medcard.services.impl;

import com.medcard.dto.doctor.DoctorRequest;
import com.medcard.dto.doctor.DoctorRequests;
import com.medcard.dto.doctor.DoctorResponse;
import com.medcard.dto.doctor.DoctorResponses;
import com.medcard.entities.Patient;
import com.medcard.entities.User;
import com.medcard.repositories.DoctorRepository;
import com.medcard.entities.Doctor;
import com.medcard.repositories.PatientRepository;
import com.medcard.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository, PatientRepository patientRepository) {
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    @Override
    public DoctorResponse save(DoctorRequest doctorRequest) {
        Doctor doctor = new Doctor();
        User user = new User();

        doctor.setUser(user);
        user.setDoctor(doctor);

        doctor.getUser().setFirstName(doctorRequest.getFirstname());
        doctor.getUser().setLastName(doctorRequest.getLastname());
        doctor.getUser().setEmail(doctorRequest.getEmail());
        doctor.getUser().setPassword(doctorRequest.getPassword());
        doctor.setSpecialization(doctorRequest.getSpecialization());

        doctorRepository.save(doctor);
        return new DoctorResponse(doctor.getId(), doctor.getUser().getFirstName());
    }

    @Override
    public DoctorResponses getById(Long id) {
        return convertEntityToResponses(doctorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("doctor with id: " + id + " is not exist!")));
    }

    @Override
    public List<DoctorResponses> getAll() {

        Iterable<Doctor> doctorsIterable = doctorRepository.findAll();
        List<Doctor> doctors = new ArrayList<>();

        for (Doctor doctor : doctorsIterable) {
            doctors.add(doctor);
        }

        return doctors.stream().map(this::convertEntityToResponses).collect(Collectors.toList());
    }

    @Override
    public void deleteById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if (doctor != null) {
            List<Patient> patients = doctor.getPatients();

            if (patients != null) {
                for (Patient patient : patients) {
                    patient.setDoctor(null);
                    patientRepository.save(patient);
                }
                doctor.setPatients(null);
            }

            doctorRepository.deleteById(id);
        }
    }


    @Override
    public DoctorResponses update(Long id, DoctorRequests doctorRequests) {

        Doctor doctor = doctorRepository.findById(id).orElseThrow(() -> new NoSuchElementException("doctor with id: " + id + " is not exist!"));

        update(doctor, doctorRequests);

        doctorRepository.save(doctor);
        return new DoctorResponses(doctor.getId(), doctor.getUser().getFirstName(), doctor.getUser().getLastName(),
                doctor.getSpecialization(), doctor.getUser().getCity(), doctor.getUser().getBirthDate(),
                doctor.getUser().getPhoneNumber(), doctor.getUser().getProfileImg(), doctor.getUser().getEmail());

    }


    private void update(Doctor doctor, DoctorRequests doctorRequests) {
        if (doctorRequests.getFirstName() != null) {
            doctor.getUser().setFirstName(doctorRequests.getFirstName());
        }
        if (doctorRequests.getLastName() != null) {
            doctor.getUser().setLastName(doctorRequests.getLastName());
        }
        if (doctorRequests.getSpecialization() != null) {
            doctor.setSpecialization(doctorRequests.getSpecialization());
        }
        if (doctorRequests.getCity() != null) {
            doctor.getUser().setCity(doctorRequests.getCity());
        }
        if (doctorRequests.getBirthDate() != null) {
            doctor.getUser().setBirthDate(doctorRequests.getBirthDate());
        }
        if (doctorRequests.getPhoneNumber() != null) {
            doctor.getUser().setPhoneNumber(doctorRequests.getPhoneNumber());
        }
        if (doctorRequests.getProfileImg() != null) {
            doctor.getUser().setProfileImg(doctorRequests.getProfileImg());
        }

        if (doctorRequests.getEmail() != null) {
            doctor.getUser().setEmail(doctorRequests.getEmail());
        }
    }


    public DoctorResponses convertEntityToResponses(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        DoctorResponses doctorResponses = new DoctorResponses();
        if (doctor.getId() != null) {
            doctorResponses.setId(doctor.getId());
        }

        doctorResponses.setFirstName(doctor.getUser().getFirstName());
        doctorResponses.setLastName(doctor.getUser().getLastName());
        doctorResponses.setSpecialization(doctor.getSpecialization());
        doctorResponses.setCity(doctor.getUser().getCity());
        doctorResponses.setBirthDate(doctor.getUser().getBirthDate());
        doctorResponses.setPhoneNumber(doctor.getUser().getPhoneNumber());
        doctorResponses.setProfileImg(doctor.getUser().getProfileImg());
        doctorResponses.setEmail(doctor.getUser().getEmail());

        return doctorResponses;
    }

}
