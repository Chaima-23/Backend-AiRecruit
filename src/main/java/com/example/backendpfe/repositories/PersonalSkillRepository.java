package com.example.backendpfe.repositories;


import com.example.backendpfe.models.ollama.CvPersonalSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalSkillRepository extends JpaRepository<CvPersonalSkill, String> {
}
