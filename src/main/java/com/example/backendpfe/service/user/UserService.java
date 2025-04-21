package com.example.backendpfe.service.user;

import com.example.backendpfe.models.idm.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);

    User findByUsername(String username);

    List<User> findAll();

    /*
    //Associe un rôle existant à un utilisateur
    User addRoleToUser(String username, String roleName);

    //Permet de sauvegarder un nouveau rôle dans la base
    Role addRole(Role role); */
}
