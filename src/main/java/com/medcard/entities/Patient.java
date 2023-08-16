package com.medcard.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<Doctor> doctors;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "patient")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Form> forms;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "patient")
    private List<History> histories;

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH, CascadeType.PERSIST}, mappedBy = "patient")
    private List<Appointment> appointments;
}
