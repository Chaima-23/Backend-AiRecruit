package com.example.backendpfe.models.test;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class MultipleChoiceQuestion extends Question {
    private String options;
    private int timeLimit;
}
