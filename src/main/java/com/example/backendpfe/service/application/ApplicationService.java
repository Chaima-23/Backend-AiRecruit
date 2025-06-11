package com.example.backendpfe.service.application;

import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.models.request.ApplicationRequest;

import java.util.List;

public interface ApplicationService {
    List<ApplicationRequest> getApplicationsByRecruiter(String recruiterId, String offerType);
    Candidate getCandidateDetails(String candidateId);
    void addApplicationToFavorites(String recruiterId, String applicationId);
    void removeApplicationFromFavorites(String recruiterId, String applicationId);
    List<ApplicationRequest> getFavoriteApplications(String recruiterId);
}