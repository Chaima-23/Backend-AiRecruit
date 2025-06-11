package com.example.backendpfe.controllers;

import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.request.ApplicationRequest;
import com.example.backendpfe.repositories.RecruiterRepository;
import com.example.backendpfe.service.application.ApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/recruiter/applications")
public class RecruiterApplicationController {

    private final ApplicationService applicationService;
    private final RecruiterRepository recruiterRepository;

    public RecruiterApplicationController(ApplicationService applicationService, RecruiterRepository recruiterRepository) {
        this.applicationService = applicationService;
        this.recruiterRepository = recruiterRepository;
    }

    // Lister toutes les candidatures pour les offres du recruteur
    @GetMapping
    public ResponseEntity<Map<String, Object>> getApplications(
            @RequestParam(required = false) String type) {
        String recruiterId = getCurrentRecruiterId();
        List<ApplicationRequest> applications = applicationService.getApplicationsByRecruiter(recruiterId, type);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Applications retrieved successfully for recruiter");
        response.put("data", applications);
        return ResponseEntity.ok(response);
    }

    // Afficher les détails d'un candidat
    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<Map<String, Object>> getCandidateDetails(@PathVariable String candidateId) {
        Candidate candidate = applicationService.getCandidateDetails(candidateId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Candidate details retrieved successfully");
        response.put("data", candidate);
        return ResponseEntity.ok(response);
    }
    // Ajouter une candidature aux favoris
    @PostMapping("/{applicationId}/favorite")
    public ResponseEntity<Map<String, Object>> addApplicationToFavorites(@PathVariable String applicationId) {
        String recruiterId = getCurrentRecruiterId();
        applicationService.addApplicationToFavorites(recruiterId, applicationId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Application added to favorites successfully");
        return ResponseEntity.ok(response);
    }

    // Retirer une candidature des favoris
    @DeleteMapping("/{applicationId}/favorite")
    public ResponseEntity<Map<String, Object>> removeApplicationFromFavorites(@PathVariable String applicationId) {
        String recruiterId = getCurrentRecruiterId();
        applicationService.removeApplicationFromFavorites(recruiterId, applicationId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Application removed from favorites successfully");
        return ResponseEntity.ok(response);
    }

    // Lister toutes les candidatures favorites
    @GetMapping("/favorites")
    public ResponseEntity<Map<String, Object>> getFavoriteApplications() {
        String recruiterId = getCurrentRecruiterId();
        List<ApplicationRequest> favorites = applicationService.getFavoriteApplications(recruiterId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Favorite applications retrieved successfully");
        response.put("data", favorites);
        return ResponseEntity.ok(response);
    }

    // Méthode pour récupérer l'ID du recruteur connecté à partir du token JWT
    private String getCurrentRecruiterId() {
        org.springframework.security.core.Authentication authentication =
                org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        org.springframework.security.oauth2.jwt.Jwt jwt = (org.springframework.security.oauth2.jwt.Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject(); // ID Keycloak
        return mapKeycloakIdToRecruiterId(keycloakId);
    }

    // Méthode pour mapper keycloakId à recruiterId
    private String mapKeycloakIdToRecruiterId(String keycloakId) {
        Recruiter recruiter = recruiterRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for Keycloak ID: " + keycloakId));
        return recruiter.getId();
    }
}