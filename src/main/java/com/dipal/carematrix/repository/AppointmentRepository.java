package com.dipal.carematrix.repository;


import com.dipal.carematrix.entity.Appointment;
import com.dipal.carematrix.entity.Doctor;
import com.dipal.carematrix.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorAndAppointmentDateTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end);
    List<Appointment> findByPatient(Patient patient);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByStatus(String status);
}
