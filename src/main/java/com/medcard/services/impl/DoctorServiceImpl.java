package com.medcard.services.impl;

import com.medcard.controllers.DoctorController;
import com.medcard.dto.DoctorCreateRequest;
import com.medcard.dto.DoctorDto;
import com.medcard.dto.DoctorUpdateRequest;

import com.medcard.dto.UserDto;
import com.medcard.entities.Patient;
import com.medcard.entities.Role;
import com.medcard.entities.User;
import com.medcard.repositories.DoctorRepository;
import com.medcard.entities.Doctor;
import com.medcard.repositories.PatientRepository;
import com.medcard.repositories.UserRepository;
import com.medcard.services.DoctorService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Service
@AllArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public void saveDoctor(DoctorCreateRequest createRequest) {
        User user = new User();
        Doctor doctor = new Doctor();
        doctor.setSpecialization(createRequest.getSpecialization());
        doctorRepository.save(doctor);
        user.setFirstname(createRequest.getFirstname());
        user.setLastname(createRequest.getLastname());
        user.setEmail(createRequest.getEmail());
        user.setPassword(passwordEncoder.encode(createRequest.getPassword()));
        user.setRole(Role.ROLE_DOCTOR);
        user.setDoctor(doctor);
        doctor.setUser(user);
        userRepository.save(user);
    }

    @Override
    public Doctor getById(Long id) {
        return doctorRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Doctor> getAll() {
        return (List<Doctor>) doctorRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        Doctor doctor = doctorRepository.findById(id).orElse(null);

        if (doctor != null) {
            List<Patient> patients = doctor.getPatients();

            if (patients != null) {
                for (Patient patient : patients) {
                    patient.getDoctors().remove(doctor);
                    patientRepository.save(patient);
                }
                doctor.setPatients(null);
            }

            doctorRepository.deleteById(id);
        }
    }


    @Override
    public Doctor update(Long id, DoctorUpdateRequest updateDoctor, String imageUUID) {
            Doctor doctor = getById(id);
            doctor.getUser().setFirstname(updateDoctor.getFirstname());
            doctor.getUser().setLastname(updateDoctor.getLastname());
            doctor.getUser().setCity(updateDoctor.getCity());
            doctor.getUser().setBirthDate(updateDoctor.getBirthDate());
            doctor.getUser().setPhoneNumber(updateDoctor.getPhoneNumber());
            doctor.setSpecialization(updateDoctor.getSpecialization());
            doctor.getUser().setProfileImg(imageUUID);
            userRepository.save(doctor.getUser());
            doctorRepository.save(doctor);
            return doctor;
    }

    @Override
    public Doctor updateForAdmin(Long id, DoctorUpdateRequest updateDoctor) {
        Doctor doctor = getById(id);
        doctor.getUser().setFirstname(updateDoctor.getFirstname());
        doctor.getUser().setLastname(updateDoctor.getLastname());
        doctor.getUser().setCity(updateDoctor.getCity());
        doctor.getUser().setBirthDate(updateDoctor.getBirthDate());
        doctor.getUser().setPhoneNumber(updateDoctor.getPhoneNumber());
        doctor.setSpecialization(updateDoctor.getSpecialization());
        userRepository.save(doctor.getUser());
        doctorRepository.save(doctor);
        return doctor;
    }
}
