package com.medcard.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "doctors")
    private List<Patient> patients;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<Appointment> appointments;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor")
    private List<Form> forms;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "doctor", orphanRemoval = true)
    private List<History> histories = new ArrayList<>();

}
