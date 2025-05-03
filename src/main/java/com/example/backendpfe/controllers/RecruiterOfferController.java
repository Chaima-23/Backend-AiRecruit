package com.example.backendpfe.controllers;

import com.example.backendpfe.models.offers.InternshipOffer;
import com.example.backendpfe.models.offers.JobOffer;
import com.example.backendpfe.models.offers.Offer;

import com.example.backendpfe.service.offer.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard/recruiter/offers")
@PreAuthorize("hasRole('RECRUITER')") // Sécurise tous les endpoints de ce contrôleur
public class RecruiterOfferController {

    private final OfferService offerService;

    public RecruiterOfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Créer une offre d'emploi
    @PostMapping("/job")
    public ResponseEntity<Offer> createJobOffer(@RequestBody JobOffer jobOffer) {
        Offer createdOffer = offerService.createJobOffer(jobOffer);
        return ResponseEntity.ok(createdOffer);
    }

    // Créer une offre de stage
    @PostMapping("/internship")
    public ResponseEntity<Offer> createInternshipOffer(@RequestBody InternshipOffer internshipOffer) {
        Offer createdOffer = offerService.createInternshipOffer(internshipOffer);
        return ResponseEntity.ok(createdOffer);
    }

    // Récupérer les offres du recruteur actuel
    @GetMapping("/my-offers")
    public ResponseEntity<List<Offer>> getOffersByCurrentRecruiter() {
        List<Offer> offers = offerService.getOffersByCurrentRecruiter();
        return ResponseEntity.ok(offers);
    }

    // Mettre à jour une offre
    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable String id, @RequestBody Offer updatedOffer) {
        Offer offer = offerService.updateOffer(id, updatedOffer);
        return ResponseEntity.ok(offer);
    }

    // Supprimer une offre
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable String id) {
        offerService.deleteOffer(id);
        return ResponseEntity.noContent().build();
    }
}