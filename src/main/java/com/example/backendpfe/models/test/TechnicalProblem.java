package com.example.backendpfe.models.test;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class TechnicalProblem extends Question {
    private int maxSubmissions;
    private String problemStatement;
}
