package com.medcard.security;

import com.medcard.entities.Doctor;
import com.medcard.entities.User;
import com.medcard.repositories.DoctorRepository;
import com.medcard.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CurrentDoctorFinder {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    public long getCurrentUserId() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        Doctor doctor = doctorRepository.findByUserEmail(username);

        if (doctor != null) {
            return doctor.getId();
        } else {
            return -1;
        }
    }
}
