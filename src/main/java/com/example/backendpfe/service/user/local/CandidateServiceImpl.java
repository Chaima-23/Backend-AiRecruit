package com.example.backendpfe.service.user.local;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.service.user.CandidateService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import com.example.backendpfe.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public Candidate registerCandidate(CandidateDTO candidateDTO, UserDTO userDTO) {

        if (userService.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userService.existsByEmail(userDTO.getEmail())) {
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
        candidate.setFirstName(userDTO.getFirstName());
        candidate.setLastName(userDTO.getLastName());
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

        // Save in Keycloak with role
        String keycloakId = keycloakAdminService.createUserAndGetId(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                "CANDIDATE"
        );
        candidate.setKeycloakId(keycloakId);

        // Save locally
        return candidateRepository.save(candidate);
    }

    private String saveFile(MultipartFile file) {
        if (file == null || file.isEmpty()) return null;

        // Validate file type
        String contentType = file.getContentType();
        if (!"application/pdf".equals(contentType)) {
            throw new RuntimeException("Only PDF files are allowed");
        }

        try {
            String directory = "Uploads/";
            Files.createDirectories(Paths.get(directory));

            // Sanitize file name
            String originalFilename = file.getOriginalFilename();
            String sanitizedFilename = UUID.randomUUID() + ".pdf"; // Use UUID to avoid conflicts
            Path filePath = Paths.get(directory + sanitizedFilename);
            Files.write(filePath, file.getBytes());
            return filePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("File saving error: " + e.getMessage());
        }
    }
}
