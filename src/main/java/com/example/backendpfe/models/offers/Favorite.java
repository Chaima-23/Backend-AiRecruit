package com.example.backendpfe.models.offers;

import com.example.backendpfe.models.idm.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Favorite {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private User candidate;

    @ManyToOne
    private Offer offer;
}
