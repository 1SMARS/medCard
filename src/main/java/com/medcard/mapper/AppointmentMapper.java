//package com.medcard.mapper;
//
//import com.medcard.dto.AppointmentDto;
//import com.medcard.dto.PatientDto;
//import com.medcard.entities.Appointment;
//import com.medcard.entities.User;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@Component
//public class AppointmentMapper implements Function<Appointment, AppointmentDto> {
//    @Override
//    public AppointmentDto apply(Appointment appointment) {
//
//            AppointmentDto appointmentDto = new AppointmentDto();
//            if (appointment.getId() != null) {
//                appointmentDto.setId(appointment.getId());
//            }
//            appointmentDto.setAppointmentTime(appointment.getAppointmentTime());
//
//            if (appointment.getPatient() != null) {
//                PatientDto patientDto = new PatientDto();
//                patientDto.setEmail(appointment.getPatient().getUser().getEmail());
//                patientDto.setFirstname(appointment.getPatient().getUser().getFirstname());
//                patientDto.setLastname(appointment.getPatient().getUser().getLastname());
//                patientDto.setCity(appointment.getPatient().getUser().getCity());
//                patientDto.setBirthDate(appointment.getPatient().getUser().getBirthDate());
//                patientDto.setPhoneNumber(appointment.getPatient().getUser().getPhoneNumber());
//                patientDto.setProfileImg(appointment.getPatient().getUser().getProfileImg());
//                appointmentDto.setPatientDto(patientDto);
//            }
//
//        return appointmentDto;
//    }
//}
