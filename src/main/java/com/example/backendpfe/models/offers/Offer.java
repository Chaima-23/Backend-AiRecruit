package com.example.backendpfe.models.offers;

import com.example.backendpfe.models.idm.Recruiter;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDate deadline;
    private String description;
    private String dutiesAndResponsibilities;
    private String field;
    private String country;
    private String city;
    private String minQualifications;
    private float salary;
    private String tools;
    private String type;
    @Enumerated(EnumType.STRING)
    private WorkMode workMode;

    private String status = "ACTIVE";

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    private Recruiter recruiter;
}