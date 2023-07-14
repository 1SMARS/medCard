package com.medcard.dto.doctor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequests {

    private String email;
    private String firstName;
    private String lastName;
    private String city;
    private String birthDate;
    private String phoneNumber;
    private String specialization;
    private String profileImg;
}
