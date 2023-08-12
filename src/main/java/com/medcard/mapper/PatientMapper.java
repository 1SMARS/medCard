package com.medcard.mapper;

import com.medcard.dto.PatientDto;
import com.medcard.entities.Patient;
import org.springframework.stereotype.Component;
import java.util.function.Function;

@Component
public class PatientMapper implements Function<Patient, PatientDto> {

    @Override
    public PatientDto apply(Patient patient) {
        return new PatientDto(
                patient.getId(),
                patient.getUser().getEmail(),
                patient.getUser().getFirstname(),
                patient.getUser().getLastname(),
                patient.getUser().getCity(),
                patient.getUser().getBirthDate(),
                patient.getUser().getPhoneNumber(),
                patient.getUser().getProfileImg()
        );
    }
}
