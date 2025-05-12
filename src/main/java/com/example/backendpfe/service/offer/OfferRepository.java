package com.example.backendpfe.service.offer;

import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.FullTimeJob;
import com.example.backendpfe.models.offers.InternshipOffer;
import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.models.offers.PartTimeJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {

        List<Offer> findByRecruiter(Recruiter recruiter);
        List<Offer> findByStatus(String status);

        //@Query définissent des requêtes JPQL personnalisées qui filtrent les offres par recruiter et par type

        @Query("SELECT o FROM Offer o WHERE o.recruiter = :recruiter AND TYPE(o) = FullTimeJob")
        List<FullTimeJob> findFullTimeJobsByRecruiter(Recruiter recruiter);

        @Query("SELECT o FROM Offer o WHERE o.recruiter = :recruiter AND TYPE(o) = PartTimeJob")
        List<PartTimeJob> findPartTimeJobsByRecruiter(Recruiter recruiter);

        @Query("SELECT o FROM Offer o WHERE o.recruiter = :recruiter AND TYPE(o) = InternshipOffer")
        List<InternshipOffer> findInternshipOffersByRecruiter(Recruiter recruiter);

        /*La méthode delete est déjà disponible via JpaRepository,
         et nous n'avons pas besoin de méthodes spécifiques pour la suppression par type,
        car la validation du type sera gérée dans le service.*/
}