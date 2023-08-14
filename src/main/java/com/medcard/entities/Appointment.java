package com.medcard.entities;
import lombok.*;
import jakarta.persistence.*;


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

    private Boolean available;

    @ManyToOne
    private Doctor doctor;

    @OneToOne
    private Patient patient;
}
