package com.dipal.carematrix.controller;


import com.dipal.carematrix.dto.DoctorDetailsDTO;
import com.dipal.carematrix.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("/list")
    public ResponseEntity<List<DoctorDetailsDTO>> getAllDoctors() {
        List<DoctorDetailsDTO> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDetailsDTO>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorDetailsDTO> doctors = doctorService.getDoctorsBySpecialization(specialization);
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDetailsDTO> getDoctorById(@PathVariable Long id) {
        DoctorDetailsDTO doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(doctor);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PostMapping("/{id}/photo")
    public ResponseEntity<DoctorDetailsDTO> updateDoctorPhoto(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        DoctorDetailsDTO doctor = doctorService.updateDoctorPhoto(id, file);
        return ResponseEntity.ok(doctor);
    }
}
