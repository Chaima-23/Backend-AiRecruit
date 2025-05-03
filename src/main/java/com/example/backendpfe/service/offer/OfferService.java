package com.example.backendpfe.service;

import com.example.backendpfe.models.offers.InternshipOffer;
import com.example.backendpfe.models.offers.JobOffer;
import com.example.backendpfe.models.offers.Offer;

import java.util.List;

public interface OfferService {
    Offer createJobOffer(JobOffer jobOffer);
    Offer createInternshipOffer(InternshipOffer internshipOffer);
    List<Offer> getOffersByCurrentRecruiter();
    Offer getOfferById(String id);
    Offer updateOffer(String id, Offer updatedOffer);
    void deleteOffer(String id);
}