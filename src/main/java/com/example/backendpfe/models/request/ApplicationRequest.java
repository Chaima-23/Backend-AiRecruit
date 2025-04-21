package com.example.backendpfe.models.request;

import java.time.LocalDate;

import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.models.offers.Offer;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ApplicationRequest {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;
    private RequestStatus status;

    @ManyToOne
    private Offer offer;

    @ManyToOne
    private Candidate candidate;
}

