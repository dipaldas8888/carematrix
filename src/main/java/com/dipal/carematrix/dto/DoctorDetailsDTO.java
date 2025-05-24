package com.dipal.carematrix.dto;



import lombok.Data;

@Data
public class DoctorDetailsDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String specialization;
    private String qualifications;
    private Integer experience;
    private String bio;
    private String photoPath;
    private Double consultationFee;
}
