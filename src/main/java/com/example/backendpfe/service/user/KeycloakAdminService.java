package com.example.backendpfe.service.user;

public interface KeycloakAdminService {
    String createUserAndGetId(String username, String email, String password);
}
