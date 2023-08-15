//package com.medcard.mapper;
//
//import com.medcard.dto.HistoryDto;
//import com.medcard.entities.History;
//import org.springframework.stereotype.Component;
//
//import java.util.function.Function;
//
//@Component
//public class HistoryMapper implements Function<History, HistoryDto> {
//
//    @Override
//    public HistoryDto apply(History history) {
//        return new HistoryDto (
//            history.getId(),
//            history.getDoctorName(),
//            history.getDoctorSurname(),
//            history.getAppointmentTime(),
//            history.getSpecialization()
//        );
//    }
//}
