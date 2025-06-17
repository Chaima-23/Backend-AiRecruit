package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.CvEducation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EducationRepository extends JpaRepository<CvEducation, String> {
}
