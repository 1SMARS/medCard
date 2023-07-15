package com.medcard.entities;

import lombok.*;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User implements UserDetails {
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

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@OneToOne(cascade = CascadeType.ALL)
	private Doctor doctor;

	@OneToOne(cascade = CascadeType.ALL)
	private Patient patient;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (role == null) {
			return Collections.singletonList(new SimpleGrantedAuthority("ROLE_DEFAULT"));
		}
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(role.name()));
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
