package com.example.backendpfe.controllers;

import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.service.offer.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/dashboard/admin/offers")
public class AdminOfferController {

    private final OfferService offerService;

    public AdminOfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Lister toutes les offres de tous les recruteurs pour un admin
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOffers() {
        List<Offer> offers = offerService.getAllOffers();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All offers retrieved successfully for admin");
        response.put("data", offers);
        return ResponseEntity.ok(response);
    }

    // Détails d'une offre spécifique pour un admin (via "Show Details")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getOfferById(@PathVariable String id) {
        Offer offer = offerService.getOfferById(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer details retrieved successfully for admin");
        response.put("data", offer);
        return ResponseEntity.ok(response);
    }
    // Supprimer une offre spécifique pour un admin
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOffer(@PathVariable String id) {
        try {
            offerService.deleteOffer(id);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Offer deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error while deleting offer: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}