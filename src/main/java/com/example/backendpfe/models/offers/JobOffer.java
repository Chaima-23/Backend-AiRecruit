package com.example.backendpfe.models.offers;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

public class JobOffer extends Offer {
    private String position;
    private int workingHours;

}
