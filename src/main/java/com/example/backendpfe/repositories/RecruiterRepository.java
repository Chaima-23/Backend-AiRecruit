package com.example.backendpfe.repositories;

import com.example.backendpfe.models.idm.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, String> {
    Optional<Recruiter> findByEmail(String email);

    @Query("SELECT r.company.country AS country, COUNT(r) AS count " +
            "FROM Recruiter r " +
            "WHERE r.company.country IS NOT NULL " +
            "GROUP BY r.company.country")
    List<CountryCount> findAllCompanyCountriesWithCount();

    interface CountryCount {
        String getCountry();
        Long getCount();
    }
    @Query("SELECT r FROM Recruiter r WHERE r.keycloakId = :keycloakId")
    Optional<Recruiter> findByKeycloakId(@Param("keycloakId") String keycloakId);
}
