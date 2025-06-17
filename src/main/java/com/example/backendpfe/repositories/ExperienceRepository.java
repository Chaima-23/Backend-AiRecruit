package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.CvExperience;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperienceRepository extends JpaRepository<CvExperience, String> {
}
