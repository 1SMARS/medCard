package com.medcard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorUpdateRequest {

    private String firstname;
    private String lastname;
    private String city;
    private String birthDate;
    private String phoneNumber;
    private String specialization;
}
