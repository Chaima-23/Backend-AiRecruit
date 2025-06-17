package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.CV;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CVRepository extends JpaRepository<CV, String> {
}
