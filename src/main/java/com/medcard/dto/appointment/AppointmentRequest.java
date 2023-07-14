package com.medcard.dto.appointment;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AppointmentRequest {

    private String appointmentTime;
    private Long patientId;
    private Long doctorId;
}
