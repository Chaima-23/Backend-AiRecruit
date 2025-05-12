package com.example.backendpfe.service.offer;

import com.example.backendpfe.models.offers.*;

import java.util.List;

public interface OfferService {
    Offer createFullTimeJob(FullTimeJob fullTimeJob);
    Offer createPartTimeJob(PartTimeJob partTimeJob);
    Offer createInternshipOffer(InternshipOffer internshipOffer);
    List<Offer> getOffersByCurrentRecruiter();
    Offer getOfferById(String id);
    Offer updateFullTimeJob(String id, FullTimeJob updateFullTimeJob);
    Offer updatePartTimeJob(String id, PartTimeJob updatePartTimeJob);
    Offer updateInternshipOffer(String id, InternshipOffer updateInternshipOffer);
    void deleteOffer(String id);
    List<Offer> getAllPublicOffers();
    List<FullTimeJob> getFullTimeOffersByCurrentRecruiter();
    List<PartTimeJob> getPartTimeOffersByCurrentRecruiter();
    List<InternshipOffer> getInternshipOffersByCurrentRecruiter();
    void deleteFullTimeJob(String id);
    void deletePartTimeJob(String id);
    void deleteInternshipOffer(String id);

}