package com.dipal.carematrix.controller;


import com.dipal.carematrix.dto.request.AppointmentRequest;
import com.dipal.carematrix.entity.Appointment;
import com.dipal.carematrix.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    @Autowired
    private AppointmentService appointmentService;

    @PreAuthorize("hasRole('PATIENT')")
    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        Appointment appointment = appointmentService.createAppointment(appointmentRequest);
        return ResponseEntity.ok(appointment);
    }

    @PreAuthorize("hasRole('PATIENT')")
    @GetMapping("/patient")
    public ResponseEntity<List<Appointment>> getPatientAppointments() {
        List<Appointment> appointments = appointmentService.getPatientAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @GetMapping("/doctor")
    public ResponseEntity<List<Appointment>> getDoctorAppointments() {
        List<Appointment> appointments = appointmentService.getDoctorAppointments();
        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasRole('DOCTOR')")
    @PutMapping("/{id}/status")
    public ResponseEntity<Appointment> updateAppointmentStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        Appointment appointment = appointmentService.updateAppointmentStatus(id, status);
        return ResponseEntity.ok(appointment);
    }
}
