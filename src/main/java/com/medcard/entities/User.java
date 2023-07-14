package com.medcard.entities;

import lombok.*;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String city;
	private String birthDate;
	private String phoneNumber;
	private String profileImg;
	private Role role;

	@OneToOne(cascade = CascadeType.ALL)
	private Doctor doctor;

	@OneToOne(cascade = CascadeType.ALL)
	private Patient patient;
}
