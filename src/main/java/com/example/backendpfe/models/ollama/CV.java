package com.example.backendpfe.models.ollama;

import com.example.backendpfe.models.ollama.test.Test;
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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> phone;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> email;

    private String name;

    private String specialization;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvEducation> education;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvExperience> experience;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cv")
    private List<CvProject> project;

    @ElementCollection
    private List<String> technicalSkills;

    @ElementCollection
    private List<String> personalSkills;

    @ElementCollection
    @CollectionTable(name = "cv_notes", joinColumns = @JoinColumn(name = "cv_id"))
    @Column(name = "note", length = 1000)
    private List<String> notes;



}
