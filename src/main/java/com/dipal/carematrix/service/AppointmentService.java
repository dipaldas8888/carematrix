package com.dipal.carematrix.service;


import com.dipal.carematrix.dto.request.AppointmentRequest;
import com.dipal.carematrix.entity.*;
import com.dipal.carematrix.exception.CustomException;
import com.dipal.carematrix.repository.AppointmentRepository;
import com.dipal.carematrix.repository.DoctorRepository;
import com.dipal.carematrix.repository.PatientRepository;
import com.dipal.carematrix.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired

    public Appointment createAppointment(AppointmentRequest appointmentRequest) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient = patientRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException("Patient not found"));

        Doctor doctor = doctorRepository.findById(appointmentRequest.getDoctorId())
                .orElseThrow(() -> new CustomException("Doctor not found"));

        // Check if appointment time is in the future
        if (appointmentRequest.getAppointmentDateTime().isBefore(LocalDateTime.now())) {
            throw new CustomException("Appointment time must be in the future");
        }

        // Check if doctor is available at the requested time
        List<Appointment> conflictingAppointments = appointmentRepository.findByDoctorAndAppointmentDateTimeBetween(
                doctor,
                appointmentRequest.getAppointmentDateTime().minusMinutes(30),
                appointmentRequest.getAppointmentDateTime().plusMinutes(30));

        if (!conflictingAppointments.isEmpty()) {
            throw new CustomException("Doctor is not available at the requested time");
        }

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setAppointmentDateTime(appointmentRequest.getAppointmentDateTime());
        appointment.setReason(appointmentRequest.getReason());
        appointment.setStatus("PENDING");



        return appointmentRepository.save(appointment);
    }

    public List<Appointment> getPatientAppointments() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Patient patient = patientRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException("Patient not found"));
        return appointmentRepository.findByPatient(patient);
    }

    public List<Appointment> getDoctorAppointments() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Doctor doctor = doctorRepository.findById(userDetails.getId())
                .orElseThrow(() -> new CustomException("Doctor not found"));
        return appointmentRepository.findByDoctor(doctor);
    }

    public Appointment updateAppointmentStatus(Long appointmentId, String status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new CustomException("Appointment not found"));

        if (!List.of("PENDING", "CONFIRMED", "CANCELLED", "COMPLETED").contains(status)) {
            throw new CustomException("Invalid status");
        }

        appointment.setStatus(status);
        return appointmentRepository.save(appointment);
    }
}
