package com.example.backendpfe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class CvDTO {
    private String name;
    private List<String> email;
    private List<String> phone;
    private String address;
    private String specialty;
    private List<EducationDTO> formations;
    private List<ExperienceDTO> experiences;
    private List<ProjectDTO> projects;
    private List<SkillDTO> skills;
    private List<LanguageDTO> languages;
    private String notes;
}
