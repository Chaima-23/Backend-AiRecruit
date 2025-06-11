package com.example.backendpfe.service.application;

import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.*;
import com.example.backendpfe.models.request.ApplicationRequest;
import com.example.backendpfe.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRequestRepository applicationRequestRepository;
    private final OfferRepository offerRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;
    private final FavoriteRepository favoriteRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationRequest> getApplicationsByRecruiter(String recruiterId, String offerType) {
        // Récupérer le Recruiter à partir de l'ID
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id: " + recruiterId));

        // Récupérer les offres du recruteur
        List<Offer> offers = offerRepository.findByRecruiter(recruiter);

        // Filtrer selon le type d'offre
        if ("Internship".equalsIgnoreCase(offerType) || "Job".equalsIgnoreCase(offerType)) {
            offers = offers.stream()
                    .filter(offer -> offerType.equalsIgnoreCase("Internship") && offer instanceof InternshipOffer ||
                            offerType.equalsIgnoreCase("Job") && (offer instanceof FullTimeJob || offer instanceof PartTimeJob))
                    .toList(); // Replace collect(Collectors.toList()) with toList()
        }

        // Récupérer les IDs des offres
        List<String> offerIds = offers.stream()
                .map(Offer::getId)
                .toList(); // Replace collect(Collectors.toList()) with toList()

        // Récupérer les candidatures associées
        return applicationRequestRepository.findByOfferIdIn(offerIds);
    }

    @Override
    @Transactional(readOnly = true)
    public Candidate getCandidateDetails(String candidateId) {
        return candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found with id: " + candidateId));
    }
    @Override
    @Transactional
    public void addApplicationToFavorites(String recruiterId, String applicationId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id: " + recruiterId));
        ApplicationRequest application = applicationRequestRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        // Vérifier si la candidature est déjà dans les favoris
        Optional<Favorite> existingFavorite = favoriteRepository.findByRecruiterAndApplicationRequest(recruiter, application);
        if (existingFavorite.isPresent()) {
            throw new RuntimeException("Application already in favorites");
        }

        // Ajouter aux favoris
        Favorite favorite = new Favorite();
        favorite.setRecruiter(recruiter);
        favorite.setApplicationRequest(application);
        favoriteRepository.save(favorite);
    }

    @Override
    @Transactional
    public void removeApplicationFromFavorites(String recruiterId, String applicationId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id: " + recruiterId));
        ApplicationRequest application = applicationRequestRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found with id: " + applicationId));

        // Vérifier si la candidature est dans les favoris
        Favorite favorite = favoriteRepository.findByRecruiterAndApplicationRequest(recruiter, application)
                .orElseThrow(() -> new RuntimeException("Application not found in favorites"));

        // Supprimer des favoris
        favoriteRepository.delete(favorite);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ApplicationRequest> getFavoriteApplications(String recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found with id: " + recruiterId));
        List<Favorite> favorites = favoriteRepository.findByRecruiter(recruiter);
        return favorites.stream()
                .map(Favorite::getApplicationRequest)
                .toList();
    }
}