package com.medcard.entities;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "forms")
public class Form {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String disease;
    private String description;

    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH,CascadeType.DETACH, CascadeType.PERSIST})
    private Patient patient;
}
