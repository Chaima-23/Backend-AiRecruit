package com.example.backendpfe.controllers;

import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.service.offer.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/candidate/offers")
public class CandidateOfferController {

    private final OfferService offerService;

    public CandidateOfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Lister toutes les offres de tous les recruteurs pour un candidat
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOffers() {
        java.util.List<Offer> offers = offerService.getAllOffers();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All offers retrieved successfully for candidate");
        response.put("data", offers);
        return ResponseEntity.ok(response);
    }

    // Détails d'une offre spécifique pour un candidat
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOfferById(@PathVariable String id) {
        Offer offer = offerService.getOfferById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer details retrieved successfully for candidate");
        response.put("data", offer);
        return ResponseEntity.ok(response);
    }
}