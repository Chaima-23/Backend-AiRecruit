package com.example.backendpfe.models.offers;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class PartTimeJob extends JobOffer {
    private String schedule;
}
