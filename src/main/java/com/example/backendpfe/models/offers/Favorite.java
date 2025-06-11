package com.example.backendpfe.models.offers;

import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.request.ApplicationRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Recruiter recruiter;

    @ManyToOne
    private ApplicationRequest applicationRequest;
}