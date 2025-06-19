package com.example.backendpfe.models.ollama.test;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 1000)
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "option_text", length = 1000)
    private List<String> options;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "question_correct_options", joinColumns = @JoinColumn(name = "question_id"))
    @Column(name = "correct_option_index")
    private List<Integer> correctOptions;

    @ManyToOne
    @JoinColumn(name = "test_id")
    @ToString.Exclude
    @JsonIgnore
    private Test test;
}
