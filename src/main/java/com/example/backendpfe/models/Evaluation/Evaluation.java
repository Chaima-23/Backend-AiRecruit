package com.example.backendpfe.models.Evaluation;
import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.models.ollama.test.Test;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Evaluation {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String feedback;
    private int acceptanceThreshold;
    private int score;

    @ManyToOne
    private Candidate candidate;

    @ManyToOne
    private Test test;
}
