package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.service.user.RecruiterService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recruiters")
@CrossOrigin(value = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class RecruiterController {

    private final RecruiterService recruiterService;

    public RecruiterController(RecruiterService recruiterService) {
        this.recruiterService = recruiterService;
    }


    public record RegisterRequest(RecruiterDTO recruiterDTO, UserDTO userDTO, String firstName, String lastName) {}

    public record UpdateRequest(RecruiterDTO recruiterDTO, UserDTO userDTO) {}

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        try {
            Recruiter createdRec = recruiterService.registerRecruiter(request.recruiterDTO(), request.userDTO());
            return ResponseEntity.ok("Recruiter registered successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{recruiterId}")
    public ResponseEntity<String> updateRecruiter(
            @PathVariable String recruiterId,
            @Valid @RequestBody UpdateRequest request) {
        try {
            Recruiter updatedRecruiter = recruiterService.updateRecruiter(recruiterId, request.recruiterDTO(), request.userDTO());
            return ResponseEntity.ok("Recruiter updated successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{recruiterId}")
    public ResponseEntity<String> deleteRecruiter(@PathVariable String recruiterId) {
        try {
            recruiterService.deleteRecruiter(recruiterId);
            return ResponseEntity.ok("Recruiter deleted successfully");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body("Cannot delete recruiter. Associated offers exist.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{recruiterId}")
    public ResponseEntity<Recruiter> getRecruiterById(@PathVariable String recruiterId) {
        return recruiterService.getRecruiterById(recruiterId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Recruiter>> getAllRecruiters() {
        try {
            List<Recruiter> recruiters = recruiterService.getAllRecruiters();
            return ResponseEntity.ok(recruiters);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}