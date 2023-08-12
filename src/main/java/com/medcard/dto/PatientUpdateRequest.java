package com.medcard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientUpdateRequest {

    private String firstname;
    private String lastname;
    private String city;
    private String birthDate;
    private String phoneNumber;
    private String profileImg;

}
