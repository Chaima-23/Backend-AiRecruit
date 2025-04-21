package com.example.backendpfe.models.idm;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Administrator extends User {
    private LocalDate createdAt;
    private String status;
}

