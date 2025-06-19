package com.example.backendpfe.models.ollama.test;

import com.example.backendpfe.models.ollama.CV;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 1000)
    private String title;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Question> questions;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cv_id", referencedColumnName = "id")
    private CV test;
}
