package com.example.backendpfe.repositories;


import com.example.backendpfe.models.ollama.test.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface QuestionRepository extends JpaRepository<Question, UUID> {
}
