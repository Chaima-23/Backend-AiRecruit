package com.example.backendpfe.models.offers;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("FULL_TIME")

public class FullTimeJob extends JobOffer {
    private String benefits;
    private String contractType;
}
