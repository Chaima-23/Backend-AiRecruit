package com.example.backendpfe.models.offers;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "job_type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("JOB")

public class JobOffer extends Offer {
    private String position;
    private int workingHours;

}
