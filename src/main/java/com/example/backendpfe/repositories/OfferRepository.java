package com.example.backendpfe.repositories;

import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.FullTimeJob;
import com.example.backendpfe.models.offers.InternshipOffer;
import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.models.offers.PartTimeJob;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {
        @Modifying
        @Query("DELETE FROM Offer o WHERE o.recruiter.id = :recruiterId")
        void deleteByRecruiterIdDirect(String recruiterId);
        List<Offer> findByRecruiter(Recruiter recruiter);
        List<Offer> findByStatus(String status);

        //@Query définissent des requêtes JPQL personnalisées qui filtrent les offres par recruiter et par type

        @Query("SELECT o FROM Offer o WHERE o.recruiter = :recruiter AND TYPE(o) = FullTimeJob")
        List<FullTimeJob> findFullTimeJobsByRecruiter(Recruiter recruiter);

        @Query("SELECT o FROM Offer o WHERE o.recruiter = :recruiter AND TYPE(o) = PartTimeJob")
        List<PartTimeJob> findPartTimeJobsByRecruiter(Recruiter recruiter);

        @Query("SELECT o FROM Offer o WHERE o.recruiter = :recruiter AND TYPE(o) = InternshipOffer")
        List<InternshipOffer> findInternshipOffersByRecruiter(Recruiter recruiter);

        @Query("SELECT COUNT(o) FROM Offer o WHERE TYPE(o) = ?1")
        long countByType(Class<? extends Offer> type);

        @Query("SELECT COUNT(jo) FROM JobOffer jo")
        long countJobOffers();

}