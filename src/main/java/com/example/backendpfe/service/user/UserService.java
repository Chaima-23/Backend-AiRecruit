package com.example.backendpfe.service.user;

import com.example.backendpfe.models.idm.User;

import java.util.List;

public interface UserService {

    User saveUser(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findAll();
    boolean existsByUsername(String username); // Add this
    boolean existsByEmail(String email);

}
