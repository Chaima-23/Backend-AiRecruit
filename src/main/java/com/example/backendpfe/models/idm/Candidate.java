package com.example.backendpfe.models.idm;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class Candidate extends User {
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
