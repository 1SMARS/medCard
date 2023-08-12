package com.medcard.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {
    private Long id;
    private String doctorName;
    private String doctorSurname;
    private String appointmentTime;
    private String specialization;
}
