package com.medcard.entities;
import lombok.*;
import jakarta.persistence.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String appointmentTime;

    @ManyToOne
    private Doctor doctor;

    @ManyToOne
    private Patient patient;
}
