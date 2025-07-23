package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, String> {
    Optional<Test> findTopByOrderByIdDesc();
    Optional<Test> findByCvId(String cvId);

}