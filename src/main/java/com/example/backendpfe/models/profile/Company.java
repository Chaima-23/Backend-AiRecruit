package com.example.backendpfe.models.profile;
import com.example.backendpfe.models.idm.Recruiter;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Company {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private int size;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String country;
    private String description;
    private String field;
    private String phoneNumber1;
    private String phoneNumber2;
    private String website;

    @OneToOne(mappedBy = "company")
    @JsonBackReference
    private Recruiter recruiter;
}

