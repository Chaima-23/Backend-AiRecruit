package com.example.backendpfe.service;

import com.example.backendpfe.models.idm.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername(String username);

}
