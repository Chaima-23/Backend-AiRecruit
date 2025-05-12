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
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @ModelAttribute CandidateDTO candidateDTO,
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String firstName,
            @RequestParam String lastName)
        {

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setEmail(email);
        userDTO.setPassword(password);
        userDTO.setFirstName(firstName);
        userDTO.setLastName(lastName);

        try {
            Candidate createdCan = candidateService.registerCandidate(candidateDTO, userDTO);
            return ResponseEntity.ok("Candidate registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
