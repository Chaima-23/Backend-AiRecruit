package com.example.backendpfe.models.test;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;


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
