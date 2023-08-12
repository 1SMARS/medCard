package com.medcard.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @OneToOne(mappedBy = "patient")
    private History history;
}
