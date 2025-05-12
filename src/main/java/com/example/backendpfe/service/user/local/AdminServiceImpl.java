package com.example.backendpfe.service.user.local;

import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Administrator;
import com.example.backendpfe.service.user.AdminService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
}
