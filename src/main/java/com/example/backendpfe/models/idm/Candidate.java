package com.example.backendpfe.models.idm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Candidate extends User {
    private String address;
    private LocalDate dateOfBirth;
    private String diploma;
    private String experience;
    private String phoneNumber;
    private String city;
    private String country;
    private String softSkills;
    private String techSkills;
}
