package com.example.backendpfe.dto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
public class CandidateDTO {
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private String city;
    private String address;
    private String phoneNumber;

    private String diploma;
    private String specialization;
    private int yearsOfExperience;
    private String technicalSkills;
    private String softSkills;


}
