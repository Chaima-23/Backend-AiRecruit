package com.example.backendpfe.models.test;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

public class Question {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;
    private String field;
    @ManyToOne
    private Test test;
    private Duration duration;

}
