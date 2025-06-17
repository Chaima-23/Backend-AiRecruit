package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.CvSkill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<CvSkill, String> {
}
