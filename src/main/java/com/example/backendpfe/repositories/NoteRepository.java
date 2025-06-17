package com.example.backendpfe.repositories;

import com.example.backendpfe.models.ollama.CvNote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<CvNote, String> {
}
