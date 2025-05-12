package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.service.user.RecruiterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api/recruiters")
    @CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public class RecruiterController {

        private final RecruiterService recruiterService;

        public RecruiterController(RecruiterService recruiterService) {
            this.recruiterService = recruiterService;
        }

        public record RegisterRequest(RecruiterDTO recruiterDTO, UserDTO userDTO, String firstName, String lastName) {
        }

        @PostMapping("/register")
        public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
            try {
                Recruiter createdRec = recruiterService.registerRecruiter(request.recruiterDTO(), request.userDTO());
                return ResponseEntity.ok("Recruiter registered successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }
    }


