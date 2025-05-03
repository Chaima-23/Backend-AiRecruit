package com.example.backendpfe.service.user.local;

import com.example.backendpfe.models.idm.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CandidateRepository extends JpaRepository<Candidate, String> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
