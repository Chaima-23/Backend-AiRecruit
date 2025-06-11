package com.example.backendpfe.models.offers;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("PART_TIME")

public class PartTimeJob extends JobOffer {
    private String schedule;
}
