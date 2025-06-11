package com.example.backendpfe.repositories;

import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.Favorite;
import com.example.backendpfe.models.request.ApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, String> {
    // Vérifier si une candidature est déjà dans les favoris d'un recruteur
    Optional<Favorite> findByRecruiterAndApplicationRequest(Recruiter recruiter, ApplicationRequest applicationRequest);

    // Récupérer tous les favoris d'un recruteur
    List<Favorite> findByRecruiter(Recruiter recruiter);
}