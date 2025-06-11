package com.example.backendpfe.repositories;

import com.example.backendpfe.models.offers.InternshipOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternshipOfferRepository extends JpaRepository<InternshipOffer, String> {
}