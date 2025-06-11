package com.example.backendpfe.models.test;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor

public class CodingQuestion extends Question {
    private String programmingLanguage;
    private int timeLimit;
}
