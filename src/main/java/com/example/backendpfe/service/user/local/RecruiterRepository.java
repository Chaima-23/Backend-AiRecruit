package com.example.backendpfe.service.user.local;

import com.example.backendpfe.models.idm.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface RecruiterRepository extends JpaRepository<Recruiter, String> {
    Optional<Recruiter> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
