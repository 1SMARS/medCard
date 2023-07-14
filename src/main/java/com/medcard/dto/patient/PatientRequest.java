package com.medcard.dto.patient;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientRequest {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
}
