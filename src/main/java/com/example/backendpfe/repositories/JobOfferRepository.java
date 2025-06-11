package com.example.backendpfe.repositories;

import com.example.backendpfe.models.offers.JobOffer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobOfferRepository extends JpaRepository<JobOffer, String> {
}