package com.example.backendpfe.models.ollama;

import jakarta.persistence.*;

import java.util.List;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvEducation> education;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvExperience> experience;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvProject> project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvSkill> technicalSkills;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvPersonalSkill> personalSkills;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cv_id")
    private List<CvNote> notes;
}
