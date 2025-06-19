package com.example.backendpfe.repositories;


import com.example.backendpfe.models.ollama.test.Question;
import org.springframework.data.jpa.repository.JpaRepository;



public interface QuestionRepository extends JpaRepository<Question, String> {
}
