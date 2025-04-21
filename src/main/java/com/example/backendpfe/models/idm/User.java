package com.example.backendpfe.models.idm;

import jakarta.persistence.*;
import lombok.*;


@Table(name="users")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String email;
    private String username;
    private String password;
    private String keycloakId;

}
