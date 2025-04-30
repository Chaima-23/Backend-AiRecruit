package com.example.backendpfe.service.user.local;

import com.example.backendpfe.service.user.KeycloakAdminService;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

@Service
public class KeycloakAdminServiceImpl implements KeycloakAdminService {
    private final String serverUrl = "http://localhost:8180";
    private final String realm = "jobs";
    private final String clientId = "admin-cli"; //pour un accès admin via API.
    private final String username = "admin";
    private final String password = "admin";


    // se connecter au serveur Keycloak avec un "admin client"
    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder() //crée une instance Keycloak
                .serverUrl(serverUrl)
                .realm(realm)
                .username(username)
                .password(password)
                .clientId(clientId)
                .build();
    }

    @Override
    public String createUserAndGetId(String username, String email, String password) {
        Keycloak keycloak = getKeycloakInstance();

        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);

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

        return userId;
    }

}
