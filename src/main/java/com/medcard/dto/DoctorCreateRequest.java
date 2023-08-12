package com.medcard.dto;

import lombok.Data;

@Data
public class DoctorCreateRequest {

    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String specialization;
}
