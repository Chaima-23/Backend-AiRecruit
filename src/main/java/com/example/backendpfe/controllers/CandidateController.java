package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.service.user.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/register")
    public ResponseEntity<String> registerCandidate(
            @ModelAttribute CandidateDTO candidateDTO,
            @ModelAttribute UserDTO userDTO) {
        try {
            Candidate createdCan = candidateService.registerCandidate(candidateDTO, userDTO);
            return ResponseEntity.ok("Candidate registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
