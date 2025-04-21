package com.example.backendpfe.models.offers;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class InternshipOffer extends Offer {
    private LocalDate startDate;
    private LocalDate endDate;

}
