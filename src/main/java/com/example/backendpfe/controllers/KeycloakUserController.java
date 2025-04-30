package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.User;
import com.example.backendpfe.service.user.UserService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class KeycloakUserController {

    private final UserService UserService;
    private final KeycloakAdminService keycloakAdminService;

    KeycloakUserController(com.example.backendpfe.service.user.UserService userService, KeycloakAdminService keycloakAdminService) {
        UserService = userService;
        this.keycloakAdminService = keycloakAdminService;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@RequestBody UserDTO userDTO) {
        try {
            String keycloakId = keycloakAdminService.createUserAndGetId(
                    userDTO.getUsername(),
                    userDTO.getEmail(),
                    userDTO.getPassword()
            );
            User user = new User();
            user.setUsername(userDTO.getUsername());
            user.setEmail(userDTO.getEmail());
            user.setPassword(userDTO.getPassword());
            user.setKeycloakId(keycloakId);

            UserService.saveUser(user);

            return ResponseEntity.ok("User created with local ID : " + keycloakId);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while creating : " + e.getMessage());
        }
    }
}

