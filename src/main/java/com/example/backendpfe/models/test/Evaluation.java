package com.example.backendpfe.models.test;
import com.example.backendpfe.models.idm.Candidate;
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
