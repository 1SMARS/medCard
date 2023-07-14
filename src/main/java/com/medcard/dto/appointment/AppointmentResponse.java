package com.medcard.dto.appointment;

import com.medcard.dto.patient.PatientResponses;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private Long id;
    private String appointmentTime;
    private PatientResponses patientResponses;
}
