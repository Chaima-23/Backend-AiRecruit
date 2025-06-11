package com.example.backendpfe.repositories;

import com.example.backendpfe.models.idm.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {

    @Query("SELECT COUNT(c) FROM Candidate c WHERE LOWER(c.gender) = LOWER(:gender)")
    long countByGender(String gender);

    long count();

}