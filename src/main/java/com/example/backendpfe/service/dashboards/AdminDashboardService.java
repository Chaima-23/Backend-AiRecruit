package com.example.backendpfe.service.dashboards;

import java.util.Map;

public interface AdminDashboardService {

    long getTotalJobOffers();
    long getTotalInternshipOffers();
    long getTotalApplications();
    long getTotalCandidates();
    long getTotalRecruiters();
    Map<String, Long> getCandidatesByGender();
    Map<String, Long> getRegisteredByCountry();
}
