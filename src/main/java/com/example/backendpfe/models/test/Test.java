package com.example.backendpfe.models.test;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Test {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDate date;
    private TestStatus status;

    @OneToMany(mappedBy = "test")
    private List<Question> questions;
}
