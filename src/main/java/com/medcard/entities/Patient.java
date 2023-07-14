package com.medcard.entities;

import lombok.*;

import jakarta.persistence.*;

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

    @ManyToOne
    private Doctor doctor;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "patient")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    private Form form;

}
