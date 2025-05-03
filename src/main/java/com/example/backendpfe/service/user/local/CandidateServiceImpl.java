package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Candidate registerCandidate(CandidateDTO candidateDTO, UserDTO userDTO) {

        if (candidateRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (candidateRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Upload files
        String cvPath = saveFile(candidateDTO.getCvFile());
        String coverLetterPath = saveFile(candidateDTO.getCoverLetterFile());

        // Create Candidate
        Candidate candidate = new Candidate();
        candidate.setUsername(userDTO.getUsername());
        candidate.setEmail(userDTO.getEmail());
        candidate.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        candidate.setDateOfBirth(candidateDTO.getDateOfBirth());
        candidate.setGender(candidateDTO.getGender());
        candidate.setCountry(candidateDTO.getCountry());
        candidate.setCity(candidateDTO.getCity());
        candidate.setAddress(candidateDTO.getAddress());
        candidate.setPhoneNumber(candidateDTO.getPhoneNumber());
        candidate.setDiploma(candidateDTO.getDiploma());
        candidate.setSpecialization(candidateDTO.getSpecialization());
        candidate.setYearsOfExperience(candidateDTO.getYearsOfExperience());
        candidate.setTechnicalSkills(candidateDTO.getTechnicalSkills());
        candidate.setSoftSkills(candidateDTO.getSoftSkills());
        candidate.setCvFilePath(cvPath);
        candidate.setCoverLetterFilePath(coverLetterPath);

        // Save in Keycloak
        String keycloakId = keycloakAdminService.createUserAndGetId(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword()
        );
        candidate.setKeycloakId(keycloakId);

        // Save locally
        return candidateRepository.save(candidate);
    }

    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        try {
            String directory = "uploads/";
            Files.createDirectories(Paths.get(directory));

            Path filePath = Paths.get(directory + file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("File saving error: " + e.getMessage());
        }
    }
}
