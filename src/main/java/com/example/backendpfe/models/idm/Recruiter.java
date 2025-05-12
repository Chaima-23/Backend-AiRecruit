package com.example.backendpfe.models.idm;
import com.example.backendpfe.models.profile.Company;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Recruiter extends User {
    @OneToOne
    private Company company;
}
