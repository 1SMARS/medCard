package com.medcard.entities;

import jakarta.persistence.*;
import lombok.*;


import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String specialization;

    @OneToOne(mappedBy = "doctor", cascade = CascadeType.ALL)
    private User user;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "doctor")
    private List<Patient> patients;

    public void addPatient(Patient patient) {
        if (patients == null) {
            patients = new ArrayList<>();
        }
        patients.add(patient);
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<Appointment> appointments;

    public void addAppointment(Appointment appointmen) {
        if (appointments == null) {
            appointments = new ArrayList<>();
        }
        appointments.add(appointmen);
    }

}
