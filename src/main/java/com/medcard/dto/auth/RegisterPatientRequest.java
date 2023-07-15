package com.medcard.dto.auth;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPatientRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
