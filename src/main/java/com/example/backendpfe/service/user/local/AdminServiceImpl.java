package com.example.backendpfe.service.user.local;

import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Administrator;
import com.example.backendpfe.models.idm.User;
import com.example.backendpfe.service.user.AdminService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Administrator createAdmin(UserDTO userDTO) {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String keycloakId = keycloakAdminService.createUserAndGetId(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                "ADMIN"
        );

        Administrator admin = new Administrator();
        admin.setUsername(userDTO.getUsername());
        admin.setEmail(userDTO.getEmail());
        admin.setFirstName(userDTO.getFirstName());
        admin.setLastName(userDTO.getLastName());
        admin.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        admin.setKeycloakId(keycloakId);
        admin.setCreatedAt(LocalDate.now());
        admin.setStatus("ACTIVE");

        return userRepository.save(admin);
    }

    @Transactional
    public Administrator updateOwnAdmin(UserDTO userDTO) {
        // Récupérer le keycloakId depuis le token JWT
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String keycloakId = jwt.getSubject(); // Le 'sub' contient l'ID de Keycloak

        // Trouver l'admin par son keycloakId
        User user = userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new RuntimeException("Admin not found with Keycloak ID: " + keycloakId));

        // Vérifier si l'utilisateur est un Administrator
        if (!(user instanceof Administrator)) {
            throw new RuntimeException("User with Keycloak ID " + keycloakId + " is not an Administrator");
        }
        Administrator admin = (Administrator) user;

        // Vérifier les conflits d'email (sauf si inchangé)
        if (userDTO.getEmail() != null && !admin.getEmail().equals(userDTO.getEmail())) {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            admin.setEmail(userDTO.getEmail());
            keycloakAdminService.updateUserEmail(admin.getKeycloakId(), userDTO.getEmail());
        }

        // Mettre à jour le mot de passe si fourni
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            keycloakAdminService.updateUserPassword(admin.getKeycloakId(), userDTO.getPassword());
        }

        return userRepository.save(admin);
    }
}
