package com.dipal.carematrix.controller;


import com.dipal.carematrix.dto.request.DoctorSignupRequest;
import com.dipal.carematrix.dto.request.LoginRequest;
import com.dipal.carematrix.dto.request.SignupRequest;
import com.dipal.carematrix.dto.response.JwtResponse;
import com.dipal.carematrix.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        authService.registerUser(signUpRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/signup/doctor")
    public ResponseEntity<?> registerDoctor(@Valid @RequestBody DoctorSignupRequest doctorSignUpRequest) {
        authService.registerDoctor(doctorSignUpRequest);
        return ResponseEntity.ok("Doctor registered successfully!");
    }
}
