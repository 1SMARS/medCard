package com.medcard.services;

import com.medcard.dto.appointment.AppointmentRequest;
import com.medcard.dto.appointment.AppointmentResponse;

import java.util.List;

public interface AppointmentService {

    AppointmentResponse save(Long doctorId, Long patientId, String appointmentTime);

    AppointmentResponse update(Long id, AppointmentRequest appointmentRequest);

    void deleteForDoctor(Long id);

    List<AppointmentResponse> getAll();

    AppointmentResponse getById(Long id);

    List<AppointmentResponse> getAppointmentsByDoctor(Long doctorId);
}
