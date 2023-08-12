package com.medcard.services.impl;


import com.medcard.dto.DoctorCreateRequest;
import com.medcard.dto.DoctorUpdateRequest;
import com.medcard.dto.UserDto;
import com.medcard.entities.Doctor;
import com.medcard.entities.Patient;
import com.medcard.entities.Role;
import com.medcard.entities.User;
import com.medcard.repositories.DoctorRepository;
import com.medcard.repositories.PatientRepository;
import com.medcard.repositories.UserRepository;
import com.medcard.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        Patient patient = new Patient();
        patientRepository.save(patient);
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.ROLE_PATIENT);
        user.setPatient(patient);
        patient.setUser(user);
        userRepository.save(user);
    }

    @Override
    public void saveUser(DoctorCreateRequest userDto) {
        User user = new User();
        Doctor doctor = new Doctor();
        doctor.setSpecialization(userDto.getSpecialization());
        doctorRepository.save(doctor);
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.ROLE_DOCTOR);
        user.setDoctor(doctor);
        doctor.setUser(user);
        userRepository.save(user);
    }

    @Override
    public Doctor update(Long id, DoctorUpdateRequest updateDoctor) {
        Doctor doctor = doctorRepository.findById(id).orElseThrow();
        doctor.getUser().setFirstname(updateDoctor.getFirstname());
        doctor.getUser().setLastname(updateDoctor.getLastname());
        doctor.getUser().setCity(updateDoctor.getCity());
        doctor.getUser().setBirthDate(updateDoctor.getBirthDate());
        doctor.getUser().setPhoneNumber(updateDoctor.getPhoneNumber());
        doctor.setSpecialization(updateDoctor.getSpecialization());
        doctor.getUser().setProfileImg(updateDoctor.getProfileImg());
        doctorRepository.save(doctor);
        return doctor;
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}