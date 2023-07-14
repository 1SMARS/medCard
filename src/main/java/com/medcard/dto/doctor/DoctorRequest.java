package com.medcard.dto.doctor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class DoctorRequest {
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String specialization;
}
