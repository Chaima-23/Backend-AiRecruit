package com.example.backendpfe.service.user;

import jakarta.ws.rs.core.Response;

public interface KeycloakAdminService {
    String createUserAndGetId(String username, String email, String password, String firstName, String lastName, String role);
    void updateUserEmail(String keycloakId, String email);
    void updateUserUsername(String keycloakId, String username);
    void updateUserPassword(String keycloakId, String password);
    void updateUserFirstName(String keycloakId, String firstName);
    void updateUserLastName(String keycloakId, String lastName);
    Response deleteUser(String keycloakId);
}