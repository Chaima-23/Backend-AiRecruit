package com.example.backendpfe.service;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository

public interface OfferRepository extends JpaRepository<Offer, String> {

        List<Offer> findByRecruiter(Recruiter recruiter);

}
