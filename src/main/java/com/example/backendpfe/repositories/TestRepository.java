package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TestRepository extends JpaRepository<Test, UUID> {
}
