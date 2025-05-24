package com.dipal.carematrix.service;


import com.dipal.carematrix.dto.DoctorDetailsDTO;
import com.dipal.carematrix.entity.Doctor;
import com.dipal.carematrix.exception.CustomException;
import com.dipal.carematrix.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    private final Path rootLocation = Paths.get("uploads");

    public List<DoctorDetailsDTO> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<DoctorDetailsDTO> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecialization(specialization).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DoctorDetailsDTO getDoctorById(Long id) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new CustomException("Doctor not found with id: " + id));
        return convertToDTO(doctor);
    }

    public DoctorDetailsDTO updateDoctorPhoto(Long id, MultipartFile file) {
        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new CustomException("Doctor not found with id: " + id));

        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            String filename = "doctor_" + id + "_" + System.currentTimeMillis() +
                    file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            Files.copy(file.getInputStream(), this.rootLocation.resolve(filename));
            doctor.setPhotoPath(filename);
            doctorRepository.save(doctor);
            return convertToDTO(doctor);
        } catch (IOException e) {
            throw new CustomException("Failed to store file: " + e.getMessage());
        }
    }

    private DoctorDetailsDTO convertToDTO(Doctor doctor) {
        DoctorDetailsDTO dto = new DoctorDetailsDTO();
        dto.setId(doctor.getId());
        dto.setUsername(doctor.getUsername());
        dto.setEmail(doctor.getEmail());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());
        dto.setPhone(doctor.getPhone());
        dto.setSpecialization(doctor.getSpecialization());
        dto.setQualifications(doctor.getQualifications());
        dto.setExperience(doctor.getExperience());
        dto.setBio(doctor.getBio());
        dto.setPhotoPath(doctor.getPhotoPath());
        dto.setConsultationFee(doctor.getConsultationFee());
        return dto;
    }
}
