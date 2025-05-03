package com.example.backendpfe.service.user.local;

import com.example.backendpfe.models.idm.User;
import com.example.backendpfe.service.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    DefaultUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }
    public User findByEmail(String email) {
        return userRepository.findByUsername(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    /*
    //Associe un rôle existant à un utilisateur
    public User addRoleToUser(String username, String roleName) {
        User user = findByUsername(username);
        Role role = roleRepository.findByRole(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
        return userRepository.save(user);
    }

    //Permet de sauvegarder un nouveau rôle dans la base
    public Role addRole(Role role) {
        return roleRepository.save(role);
    }
    */

}

