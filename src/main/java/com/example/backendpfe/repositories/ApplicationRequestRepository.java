package com.example.backendpfe.repositories;

import com.example.backendpfe.models.request.ApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationRequestRepository extends JpaRepository<ApplicationRequest, String> {
    List<ApplicationRequest> findByOfferIdIn(List<String> offerIds);
}
