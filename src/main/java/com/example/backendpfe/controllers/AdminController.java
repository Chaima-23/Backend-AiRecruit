package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Administrator;
import com.example.backendpfe.service.user.AdminService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createAdmin(@RequestBody UserDTO userDTO) {
        try {
            Administrator admin = adminService.createAdmin(userDTO);
            return ResponseEntity.ok("Admin user created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error while creating admin: " + e.getMessage());
        }
    }
    @PutMapping("/admin/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> updateOwnAdmin(@RequestBody UserDTO userDTO) {
        try {
            Administrator updatedAdmin = adminService.updateOwnAdmin(userDTO);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Email and password updated successfully");
            response.put("data", Map.of("email", updatedAdmin.getEmail()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error while updating email or password: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

}