package com.example.backendpfe.service.dashboards;

import com.example.backendpfe.models.offers.InternshipOffer;
import com.example.backendpfe.models.offers.JobOffer;
import com.example.backendpfe.repositories.ApplicationRequestRepository;
import com.example.backendpfe.repositories.OfferRepository;
import com.example.backendpfe.repositories.CandidateRepository;
import com.example.backendpfe.repositories.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final OfferRepository offerRepository;
    private final ApplicationRequestRepository applicationRequestRepository;
    private final CandidateRepository candidateRepository;
    private final RecruiterRepository recruiterRepository;

    @Override
    public long getTotalJobOffers() {
        return offerRepository.countJobOffers();
    }

    @Override
    public long getTotalInternshipOffers() {
        return offerRepository.countByType(InternshipOffer.class);
    }

    @Override
    public long getTotalApplications() {
        return applicationRequestRepository.count();
    }

    @Override
    public long getTotalCandidates() {
        return candidateRepository.count();
    }

    @Override
    public long getTotalRecruiters() {
        return recruiterRepository.count();
    }

    @Override
    public Map<String, Long> getCandidatesByGender() {
        Map<String, Long> genderStats = new HashMap<>();
        long femaleCount = candidateRepository.countByGender("Female");
        long maleCount = candidateRepository.countByGender("Male");
        genderStats.put("Females", femaleCount);
        genderStats.put("Males", maleCount);
        System.out.println("Gender stats: Females=" + femaleCount + ", Males=" + maleCount);
        return genderStats;
    }

    @Override
    public Map<String, Long> getRegisteredByCountry() {
        Map<String, Long> countryStats = new HashMap<>();
        recruiterRepository.findAllCompanyCountriesWithCount().forEach(entry -> {
            String country = entry.getCountry();
            Long count = entry.getCount();
            if (country != null) {
                countryStats.put(country, count);
            }
        });
        return countryStats;
    }
}