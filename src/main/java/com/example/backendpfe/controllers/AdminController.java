package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Administrator;
import com.example.backendpfe.service.user.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
}