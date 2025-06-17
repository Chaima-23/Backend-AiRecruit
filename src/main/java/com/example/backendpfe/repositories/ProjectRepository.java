package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.CvProject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<CvProject, String> {
}
