package com.example.backendpfe.controllers;

import com.example.backendpfe.models.offers.*;
import com.example.backendpfe.service.offer.OfferService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard/recruiter/offers")
public class RecruiterOfferController {

    private final OfferService offerService;

    public RecruiterOfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Crée une nouvelle offre d'emploi full-time avec message de succès
    @PostMapping(value = "/job/fulltime", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> createFullTimeJob(@RequestBody FullTimeJob fullTimeJob) {
        Offer created = offerService.createFullTimeJob(fullTimeJob);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer created successfully");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    // Crée une nouvelle offre d'emploi part-time avec message de succès
    @PostMapping("/job/parttime")
    public ResponseEntity<Map<String, Object>> createPartTimeJob(@RequestBody PartTimeJob partTimeJob) {
        Offer created = offerService.createPartTimeJob(partTimeJob);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer created successfully");
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    // Crée une nouvelle offre de stage avec message de succès
    @PostMapping("/internship")
    public ResponseEntity<Map<String, Object>> createInternshipOffer(@RequestBody InternshipOffer internshipOffer) {
        Offer createdOffer = offerService.createInternshipOffer(internshipOffer);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer created successfully");
        response.put("data", createdOffer);
        return ResponseEntity.ok(response);
    }

    // Récupère toutes les offres du recruteur actuel (tous types) avec message de succès
    @GetMapping("/my-offers")
    public ResponseEntity<Map<String, Object>> getOffersByCurrentRecruiter() {
        List<Offer> offers = offerService.getOffersByCurrentRecruiter();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offers retrieved successfully");
        response.put("data", offers);
        return ResponseEntity.ok(response);
    }

    // Récupère uniquement les offres full-time du recruteur actuel avec message de succès
    @GetMapping("/my-offers/job/fulltime")
    public ResponseEntity<Map<String, Object>> getFullTimeOffersByCurrentRecruiter() {
        List<FullTimeJob> offers = offerService.getFullTimeOffersByCurrentRecruiter();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Full-time offers retrieved successfully");
        response.put("data", offers);
        return ResponseEntity.ok(response);
    }

    // Récupère uniquement les offres part-time du recruteur actuel avec message de succès
    @GetMapping("/my-offers/job/parttime")
    public ResponseEntity<Map<String, Object>> getPartTimeOffersByCurrentRecruiter() {
        List<PartTimeJob> offers = offerService.getPartTimeOffersByCurrentRecruiter();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Part-time offers retrieved successfully");
        response.put("data", offers);
        return ResponseEntity.ok(response);
    }

    // Récupère uniquement les offres de stage du recruteur actuel avec message de succès
    @GetMapping("/my-offers/internship")
    public ResponseEntity<Map<String, Object>> getInternshipOffersByCurrentRecruiter() {
        List<InternshipOffer> offers = offerService.getInternshipOffersByCurrentRecruiter();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Internship offers retrieved successfully");
        response.put("data", offers);
        return ResponseEntity.ok(response);
    }

    // Met à jour une offre d'emploi full-time avec message de succès
    @PutMapping("/job/fulltime/{id}")
    public ResponseEntity<Map<String, Object>> updateFullTimeJob(@PathVariable String id, @RequestBody FullTimeJob updatedOffer) {
        Offer offer = offerService.updateFullTimeJob(id, updatedOffer);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer updated successfully");
        response.put("data", offer);
        return ResponseEntity.ok(response);
    }

    // Met à jour une offre d'emploi part-time avec message de succès
    @PutMapping("/job/parttime/{id}")
    public ResponseEntity<Map<String, Object>> updatePartTimeJob(@PathVariable String id, @RequestBody PartTimeJob updatedOffer) {
        Offer offer = offerService.updatePartTimeJob(id, updatedOffer);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer updated successfully");
        response.put("data", offer);
        return ResponseEntity.ok(response);
    }

    // Met à jour une offre de stage avec message de succès
    @PutMapping("/internship/{id}")
    public ResponseEntity<Map<String, Object>> updateInternshipOffer(@PathVariable String id, @RequestBody InternshipOffer updatedOffer) {
        Offer offer = offerService.updateInternshipOffer(id, updatedOffer);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer updated successfully");
        response.put("data", offer);
        return ResponseEntity.ok(response);
    }

    // Supprime une offre (tous types confondus) avec message de succès
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOffer(@PathVariable String id) {
        offerService.deleteOffer(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Offer deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Supprime une offre d'emploi full-time avec message de succès
    @DeleteMapping("/job/fulltime/{id}")
    public ResponseEntity<Map<String, Object>> deleteFullTimeJob(@PathVariable String id) {
        offerService.deleteFullTimeJob(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Full-time offer deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Supprime une offre d'emploi part-time avec message de succès
    @DeleteMapping("/job/parttime/{id}")
    public ResponseEntity<Map<String, Object>> deletePartTimeJob(@PathVariable String id) {
        offerService.deletePartTimeJob(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Part-time offer deleted successfully");
        return ResponseEntity.ok(response);
    }

    // Supprime une offre de stage avec message de succès
    @DeleteMapping("/internship/{id}")
    public ResponseEntity<Map<String, Object>> deleteInternshipOffer(@PathVariable String id) {
        offerService.deleteInternshipOffer(id);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Internship offer deleted successfully");
        return ResponseEntity.ok(response);
    }
}