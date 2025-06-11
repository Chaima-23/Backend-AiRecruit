package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.service.user.CandidateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@RequiredArgsConstructor
public class CandidateController {

    private final CandidateService candidateService;

    public record RegisterRequest(@Valid CandidateDTO candidateDTO, @Valid UserDTO userDTO) {}
    public record UpdateRequest(@Valid CandidateDTO candidateDTO, @Valid UserDTO userDTO) {}

    // Enregistre un nouveau candidat avec les informations fournies
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        try {
            Candidate createdCandidate = candidateService.registerCandidate(request.candidateDTO(), request.userDTO());
            return ResponseEntity.ok("Candidate registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Met à jour les informations d'un candidat spécifique (réservé au rôle CANDIDATE)
    @PutMapping("/{candidateId}")
    public ResponseEntity<String> updateCandidate(@PathVariable String candidateId, @RequestBody @Valid UpdateRequest request) {
        try {
            candidateService.updateCandidate(candidateId, request.candidateDTO(), request.userDTO());
            return ResponseEntity.ok("Candidate updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Supprime un candidat spécifique (réservé au rôle ADMIN)
    @DeleteMapping("/{candidateId}")
    public ResponseEntity<String> deleteCandidate(@PathVariable String candidateId) {
        try {
            candidateService.deleteCandidate(candidateId);
            return ResponseEntity.ok("Candidate deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Récupère la liste de tous les candidats (réservé au rôle ADMIN)
    @GetMapping
    public ResponseEntity<List<Candidate>> getAllCandidates() {
        try {
            List<Candidate> candidates = candidateService.getAllCandidates();
            return ResponseEntity.ok(candidates);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    // Récupère les détails d'un candidat spécifique par son ID (réservé au rôle ADMIN)
    @GetMapping("/{candidateId}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable String candidateId) {
        return candidateService.getCandidateById(candidateId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}