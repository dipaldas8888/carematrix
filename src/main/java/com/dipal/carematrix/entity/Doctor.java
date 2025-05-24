package com.dipal.carematrix.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "doctors")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class Doctor extends User {
    @Column(nullable = false)
    private String specialization;

    @Column
    private String qualifications;

    @Column
    private Integer experience;

    @Column
    private String bio;

    @Column
    private String photoPath; // Path to stored photo

    @Column(nullable = false)
    private Double consultationFee;
}