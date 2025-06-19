package com.example.backendpfe.controllers;

import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.service.offer.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/offers")
public class PublicOfferController {

    private final OfferService offerService;

    public PublicOfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    // Lister toutes les offres (publiques)
    @GetMapping
    public ResponseEntity<List<Offer>> getAllOffers() {
        List<Offer> offers = offerService.getAllPublicOffers();
        return ResponseEntity.ok(offers);
    }


}