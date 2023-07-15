package com.medcard.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medcard.dto.auth.*;
import com.medcard.entities.Doctor;
import com.medcard.entities.Patient;
import com.medcard.entities.Role;
import com.medcard.entities.User;
import com.medcard.repositories.DoctorRepository;
import com.medcard.repositories.PatientRepository;
import com.medcard.repositories.UserRepository;
import com.medcard.security.configs.JwtService;
import com.medcard.security.token.Token;
import com.medcard.security.token.TokenRepository;
import com.medcard.security.token.TokenType;
import com.medcard.services.DoctorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  private final DoctorRepository doctorRepository;

  public AuthenticationResponse adminRegister(RegisterRequest request) {
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException(request.getEmail() + " already exists");
    }
    User user = new User();
    user.setFirstName(request.getFirstname());
    user.setLastName(request.getLastname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_ADMIN);
    repository.save(user);

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }

  public AuthenticationResponse patientRegister(RegisterPatientRequest request) {
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException(request.getEmail() + " already exists");
    }
    User user = new User();
    user.setFirstName(request.getFirstname());
    user.setLastName(request.getLastname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_PATIENT);

    Patient patient = new Patient();
    patient.setUser(user);
    user.setPatient(patient);
    repository.save(user);

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }

  public AuthenticationResponse doctorRegister(RegisterDoctorRequest request) {
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      throw new RuntimeException(request.getEmail() + " already exists");
    }
    User user = new User();
    user.setFirstName(request.getFirstname());
    user.setLastName(request.getLastname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.setRole(Role.ROLE_DOCTOR);

    Doctor doctor = new Doctor();
    doctor.setSpecialization(request.getSpecialization());
    doctorRepository.save(doctor);
    doctor.setUser(user);
    user.setDoctor(doctor);
    repository.save(user);

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken).build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    try {
      authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(
                      request.getEmail(),
                      request.getPassword()
              )
      );
    } catch (BadCredentialsException ex) {
      throw new BadCredentialsException("Invalid email or password");
    }
    var user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow(() -> new RuntimeException("User not found"));
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
    response.getOutputStream().close();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
            .user(user)
            .token(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}
