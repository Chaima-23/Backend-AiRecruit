package com.example.backendpfe.controllers;

import com.example.backendpfe.service.dashboards.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AdminDashboardController {

    private  final AdminDashboardService dashboardService;

    @GetMapping("/stats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalJobOffers", dashboardService.getTotalJobOffers());
        stats.put("totalInternshipOffers", dashboardService.getTotalInternshipOffers());
        stats.put("totalApplications", dashboardService.getTotalApplications());
        stats.put("totalCandidates", dashboardService.getTotalCandidates());
        stats.put("totalRecruiters", dashboardService.getTotalRecruiters());
        stats.put("candidatesByGender", dashboardService.getCandidatesByGender());
        stats.put("registeredByCountry", dashboardService.getRegisteredByCountry());
        return stats;
    }
}