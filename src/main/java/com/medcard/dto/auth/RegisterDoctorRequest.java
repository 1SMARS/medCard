package com.medcard.dto.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDoctorRequest {

    private String firstname;
    private String lastname;
    private String specialization;
    private String email;
    private String password;

}
