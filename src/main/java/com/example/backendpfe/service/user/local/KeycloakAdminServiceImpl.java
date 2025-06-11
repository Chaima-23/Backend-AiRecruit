package com.example.backendpfe.service.user.local;

import com.example.backendpfe.service.user.KeycloakAdminService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import jakarta.ws.rs.core.Response;

import java.util.Collections;

@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {
    private final String serverUrl = "http://localhost:8180";
    private final String realm = "Jobs";
    private final String clientId = "admin-cli";
    private final String username = "admin";
    private final String password = "admin";

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .grantType("password")
                .clientId(clientId)
                .build();
    }

    @Override
    public String createUserAndGetId(String username, String email, String password, String firstName, String lastName, String role) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEnabled(true);
        user.setEmailVerified(true);

        Response response = keycloak.realm(realm).users().create(user);
        if (response.getStatus() != 201) {
            throw new RuntimeException("User creation error : " + response.getStatus());
        }

        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        keycloak.realm(realm).users().get(userId).resetPassword(passwordCred);

        RoleRepresentation roleRepresentation = keycloak.realm(realm).roles().get(role).toRepresentation();
        keycloak.realm(realm).users().get(userId).roles().realmLevel()
                .add(Collections.singletonList(roleRepresentation));

        return userId;
    }

    @Override
    public void updateUserEmail(String keycloakId, String email) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        user.setEmail(email);
        user.setEmailVerified(true);
        keycloak.realm(realm).users().get(keycloakId).update(user);
    }

    @Override
    public void updateUserUsername(String keycloakId, String username) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        user.setUsername(username);
        keycloak.realm(realm).users().get(keycloakId).update(user);
    }

    @Override
    public void updateUserPassword(String keycloakId, String password) {
        Keycloak keycloak = getKeycloakInstance();
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        keycloak.realm(realm).users().get(keycloakId).resetPassword(passwordCred);
    }

    @Override
    public void updateUserFirstName(String keycloakId, String firstName) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        user.setFirstName(firstName);
        keycloak.realm(realm).users().get(keycloakId).update(user);
    }

    @Override
    public void updateUserLastName(String keycloakId, String lastName) {
        Keycloak keycloak = getKeycloakInstance();
        UserRepresentation user = keycloak.realm(realm).users().get(keycloakId).toRepresentation();
        user.setLastName(lastName);
        keycloak.realm(realm).users().get(keycloakId).update(user);
    }

    @Override
    public Response deleteUser(String keycloakId) {
        Keycloak keycloak = getKeycloakInstance();
        keycloak.realm(realm).users().get(keycloakId).remove();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}