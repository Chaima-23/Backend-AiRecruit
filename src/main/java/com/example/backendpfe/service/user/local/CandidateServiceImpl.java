package com.example.backendpfe.service.user.local;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;
import com.example.backendpfe.repositories.CandidateRepository;
import com.example.backendpfe.service.user.CandidateService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import com.example.backendpfe.service.user.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CandidateServiceImpl implements CandidateService {

    private static final Logger logger = LoggerFactory.getLogger(CandidateServiceImpl.class);

    private final CandidateRepository candidateRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    // Enregistre un nouveau candidat dans la base de données et Keycloak
    @Transactional
    @Override
    public Candidate registerCandidate(CandidateDTO candidateDTO, UserDTO userDTO) {
        logger.info("Registering candidate with username: {}", userDTO.getUsername());
        if (userService.existsByUsername(userDTO.getUsername())) {
            logger.warn("Username already exists: {}", userDTO.getUsername());
            throw new RuntimeException("Username already exists");
        }
        if (userService.existsByEmail(userDTO.getEmail())) {
            logger.warn("Email already exists: {}", userDTO.getEmail());
            throw new RuntimeException("Email already exists");
        }

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

        String keycloakId = keycloakAdminService.createUserAndGetId(
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getPassword(),
                userDTO.getFirstName(),
                userDTO.getLastName(),
                "CANDIDATE"
        );
        candidate.setKeycloakId(keycloakId);

        logger.info("Saving candidate with keycloakId: {}", keycloakId);
        return candidateRepository.save(candidate);
    }

    // Met à jour les informations d'un candidat existant, uniquement si l'utilisateur est autorisé
    @Transactional
    @Override
    public void updateCandidate(String candidateId, CandidateDTO candidateDTO, UserDTO userDTO) {
        logger.info("Updating candidate with ID: {}", candidateId);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        String currentUserEmail = extractEmailFromToken();
        if (!candidate.getEmail().equals(currentUserEmail)) {
            logger.warn("Unauthorized update attempt for candidate ID: {}", candidateId);
            throw new RuntimeException("Unauthorized: You can only update your own profile");
        }

        // Mise à jour des informations de l'utilisateur
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(candidate.getEmail())) {
            if (userService.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            candidate.setEmail(userDTO.getEmail());
            keycloakAdminService.updateUserEmail(candidate.getKeycloakId(), userDTO.getEmail());
        }
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(candidate.getUsername())) {
            if (userService.existsByUsername(userDTO.getUsername())) {
                throw new RuntimeException("Username already exists");
            }
            candidate.setUsername(userDTO.getUsername());
            keycloakAdminService.updateUserUsername(candidate.getKeycloakId(), userDTO.getUsername());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            candidate.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            keycloakAdminService.updateUserPassword(candidate.getKeycloakId(), userDTO.getPassword());
        }
        if (userDTO.getFirstName() != null) {
            candidate.setFirstName(userDTO.getFirstName());
            keycloakAdminService.updateUserFirstName(candidate.getKeycloakId(), userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            candidate.setLastName(userDTO.getLastName());
            keycloakAdminService.updateUserLastName(candidate.getKeycloakId(), userDTO.getLastName());
        }

        // Mise à jour des informations du candidat
        if (candidateDTO.getDateOfBirth() != null) candidate.setDateOfBirth(candidateDTO.getDateOfBirth());
        if (candidateDTO.getGender() != null) candidate.setGender(candidateDTO.getGender());
        if (candidateDTO.getCountry() != null) candidate.setCountry(candidateDTO.getCountry());
        if (candidateDTO.getCity() != null) candidate.setCity(candidateDTO.getCity());
        if (candidateDTO.getAddress() != null) candidate.setAddress(candidateDTO.getAddress());
        if (candidateDTO.getPhoneNumber() != null) candidate.setPhoneNumber(candidateDTO.getPhoneNumber());
        if (candidateDTO.getDiploma() != null) candidate.setDiploma(candidateDTO.getDiploma());
        if (candidateDTO.getSpecialization() != null) candidate.setSpecialization(candidateDTO.getSpecialization());
        if (candidateDTO.getYearsOfExperience() != 0) candidate.setYearsOfExperience(candidateDTO.getYearsOfExperience());
        if (candidateDTO.getTechnicalSkills() != null) candidate.setTechnicalSkills(candidateDTO.getTechnicalSkills());
        if (candidateDTO.getSoftSkills() != null) candidate.setSoftSkills(candidateDTO.getSoftSkills());

        candidateRepository.save(candidate);
        logger.info("Successfully updated candidate with ID: {}", candidateId);
    }

    // Supprime un candidat et son utilisateur dans Keycloak
    @Transactional
    @Override
    public void deleteCandidate(String candidateId) {
        logger.info("Deleting candidate with ID: {}", candidateId);
        Candidate candidate = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        keycloakAdminService.deleteUser(candidate.getKeycloakId());

        candidateRepository.delete(candidate);
        logger.info("Successfully deleted candidate with ID: {}", candidateId);
    }

    // Récupère la liste de tous les candidats
    @Override
    public List<Candidate> getAllCandidates() {
        logger.info("Fetching all candidates");
        List<Candidate> candidates = candidateRepository.findAll();
        logger.info("Found {} candidates", candidates.size());
        return candidates;
    }

    // Récupère un candidat spécifique par son ID
    @Override
    public Optional<Candidate> getCandidateById(String candidateId) {
        logger.info("Fetching candidate with ID: {}", candidateId);
        Optional<Candidate> candidate = candidateRepository.findById(candidateId);
        if (candidate.isPresent()) {
            logger.info("Candidate found: {}", candidateId);
        } else {
            logger.warn("Candidate not found: {}", candidateId);
        }
        return candidate;
    }

    // Extrait l'email de l'utilisateur à partir du token JWT
    private String extractEmailFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaim("email");
        }
        return null;
    }
}