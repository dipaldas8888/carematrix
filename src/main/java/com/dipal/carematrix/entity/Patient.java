package com.dipal.carematrix.entity;



import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "patients")
@Data
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "user_id")
public class Patient extends User {
    @Column
    private String bloodGroup;

    @Column
    private Double height;

    @Column
    private Double weight;

    @Column
    private String allergies;

    @Column
    private String medicalHistory;
}
