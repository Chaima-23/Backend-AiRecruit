package com.example.backendpfe.models.Answer;
import com.example.backendpfe.models.ollama.test.Question;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Answer {
        @Id
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;
        private String content;
        @ManyToOne
        private Question question;

}
