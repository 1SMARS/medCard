package com.medcard.controllers;

import com.medcard.dto.auth.*;
import com.medcard.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

  private final AuthenticationService service;

  @PostMapping("/register/patient")
  public ResponseEntity<AuthenticationResponse> patientRegister(@RequestBody RegisterPatientRequest request) {
    return ResponseEntity.ok(service.patientRegister(request));
  }
  @PostMapping("/register/admin")
  public ResponseEntity<AuthenticationResponse> adminRegister(@RequestBody RegisterRequest request) {
    return ResponseEntity.ok(service.adminRegister(request));
  }
  @PostMapping("/register/doctor")
  public ResponseEntity<AuthenticationResponse> doctorRegister(@RequestBody RegisterDoctorRequest request) {
    return ResponseEntity.ok(service.doctorRegister(request));
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponse> authenticate(
          @RequestBody AuthenticationRequest request
  ) {
    return ResponseEntity.ok(service.authenticate(request));
  }

  @PostMapping("/refresh-token")
  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    service.refreshToken(request, response);
  }
//  @PostMapping("email_sender/send")
//  public void sender(@RequestParam String email, @RequestParam String code_or_word){
//    emailSenderService.sendEmail(email,"something",code_or_word,1L);
//  }


}

