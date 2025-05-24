package com.dipal.carematrix.service;


import com.dipal.carematrix.dto.request.DoctorSignupRequest;
import com.dipal.carematrix.dto.request.LoginRequest;
import com.dipal.carematrix.dto.request.SignupRequest;
import com.dipal.carematrix.dto.response.JwtResponse;
import com.dipal.carematrix.entity.*;
import com.dipal.carematrix.repository.RoleRepository;
import com.dipal.carematrix.repository.UserRepository;
import com.dipal.carematrix.security.JwtUtils;
import com.dipal.carematrix.security.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    public JwtResponse authenticateUser(@Valid LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles.get(0));
    }

    public void registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getPhone());

        Set<String> strRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    case "doctor":
                        Role modRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_PATIENT)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);
    }

    public void registerDoctor(DoctorSignupRequest doctorSignUpRequest) {
        if (userRepository.existsByUsername(doctorSignUpRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        if (userRepository.existsByEmail(doctorSignUpRequest.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }

        // Create new doctor's account
        Doctor doctor = new Doctor();
        doctor.setUsername(doctorSignUpRequest.getUsername());
        doctor.setEmail(doctorSignUpRequest.getEmail());
        doctor.setPassword(encoder.encode(doctorSignUpRequest.getPassword()));
        doctor.setFirstName(doctorSignUpRequest.getFirstName());
        doctor.setLastName(doctorSignUpRequest.getLastName());
        doctor.setPhone(doctorSignUpRequest.getPhone());
        doctor.setSpecialization(doctorSignUpRequest.getSpecialization());
        doctor.setQualifications(doctorSignUpRequest.getQualifications());
        doctor.setExperience(doctorSignUpRequest.getExperience());
        doctor.setBio(doctorSignUpRequest.getBio());
        doctor.setConsultationFee(doctorSignUpRequest.getConsultationFee());

        Set<Role> roles = new HashSet<>();
        Role doctorRole = roleRepository.findByName(ERole.ROLE_DOCTOR)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(doctorRole);

        doctor.setRoles(roles);
        userRepository.save(doctor);
    }
}
