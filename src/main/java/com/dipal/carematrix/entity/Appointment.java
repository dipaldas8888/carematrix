package com.dipal.carematrix.entity;



import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime appointmentDateTime;

    @Column
    private LocalDateTime endDateTime;

    @Column
    private String reason;

    @Column
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED

    @Column
    private String notes;


}
