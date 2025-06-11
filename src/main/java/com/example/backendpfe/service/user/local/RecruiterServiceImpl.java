package com.example.backendpfe.service.user.local;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.offers.Offer;
import com.example.backendpfe.models.profile.Company;
import com.example.backendpfe.repositories.CompanyRepository;
import com.example.backendpfe.repositories.OfferRepository;
import com.example.backendpfe.repositories.RecruiterRepository;
import com.example.backendpfe.service.user.RecruiterService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import com.example.backendpfe.service.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private static final Logger logger = LoggerFactory.getLogger(RecruiterServiceImpl.class);

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final OfferRepository offerRepository;

    @Transactional
    @Override
    public Recruiter registerRecruiter(RecruiterDTO recruiterDTO, UserDTO userDTO) {
        if (userService.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Nom d'utilisateur déjà existant");
        }
        if (userService.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email déjà existant");
        }

        Company company = new Company();
        company.setName(recruiterDTO.getCompanyName());
        company.setSize(recruiterDTO.getCompanySize());
        company.setCountry(recruiterDTO.getCountry());
        company.setCity(recruiterDTO.getCity());
        company.setAddressLine1(recruiterDTO.getAddressLine1());
        company.setAddressLine2(recruiterDTO.getAddressLine2());
        company.setPhoneNumber1(recruiterDTO.getPhoneNumber1());
        company.setPhoneNumber2(recruiterDTO.getPhoneNumber2());
        company.setWebsite(recruiterDTO.getWebsite());
        company.setField(recruiterDTO.getField());
        company.setDescription(recruiterDTO.getDescription());
        companyRepository.save(company);

        String keycloakId = keycloakAdminService.createUserAndGetId(
                userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword(),
                userDTO.getFirstName(), userDTO.getLastName(), "RECRUITER");

        Recruiter recruiter = new Recruiter();
        recruiter.setUsername(userDTO.getUsername());
        recruiter.setEmail(userDTO.getEmail());
        recruiter.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        recruiter.setFirstName(userDTO.getFirstName());
        recruiter.setLastName(userDTO.getLastName());
        recruiter.setKeycloakId(keycloakId);
        recruiter.setCompany(company);
        recruiter = recruiterRepository.save(recruiter);

        company.setRecruiter(recruiter);
        companyRepository.save(company);

        return recruiter;
    }

    @Override
    public Recruiter getCurrentRecruiter() {
        String email = extractEmailFromToken();
        if (email == null) {
            throw new IllegalStateException("Email non trouvé dans le token");
        }
        logger.info("Recherche du recruteur avec l'email : {}", email);
        return recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruteur non trouvé"));
    }

    private String extractEmailFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaim("email");
        }
        return null;
    }

    @Transactional
    @Override
    public Recruiter updateRecruiter(String recruiterId, RecruiterDTO recruiterDTO, UserDTO userDTO) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruteur non trouvé"));

        String currentUserEmail = extractEmailFromToken();
        if (!recruiter.getEmail().equals(currentUserEmail)) {
            throw new RuntimeException("Non autorisé : Vous ne pouvez mettre à jour que votre propre profil");
        }

        // Mise à jour des informations de l'utilisateur
        if (userDTO.getEmail() != null && !userDTO.getEmail().equals(recruiter.getEmail())) {
            if (userService.existsByEmail(userDTO.getEmail())) {
                throw new RuntimeException("Email déjà existant");
            }
            recruiter.setEmail(userDTO.getEmail());
            keycloakAdminService.updateUserEmail(recruiter.getKeycloakId(), userDTO.getEmail());
        }
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(recruiter.getUsername())) {
            if (userService.existsByUsername(userDTO.getUsername())) {
                throw new RuntimeException("Nom d'utilisateur déjà existant");
            }
            recruiter.setUsername(userDTO.getUsername());
            keycloakAdminService.updateUserUsername(recruiter.getKeycloakId(), userDTO.getUsername());
        }
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            recruiter.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            keycloakAdminService.updateUserPassword(recruiter.getKeycloakId(), userDTO.getPassword());
        }
        if (userDTO.getFirstName() != null) {
            recruiter.setFirstName(userDTO.getFirstName());
            keycloakAdminService.updateUserFirstName(recruiter.getKeycloakId(), userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null) {
            recruiter.setLastName(userDTO.getLastName());
            keycloakAdminService.updateUserLastName(recruiter.getKeycloakId(), userDTO.getLastName());
        }

        // Mise à jour des informations de l'entreprise
        Company company = recruiter.getCompany();
        if (recruiterDTO.getCompanyName() != null) company.setName(recruiterDTO.getCompanyName());
        if (recruiterDTO.getCompanySize() != 0) company.setSize(recruiterDTO.getCompanySize());
        if (recruiterDTO.getCountry() != null) company.setCountry(recruiterDTO.getCountry());
        if (recruiterDTO.getCity() != null) company.setCity(recruiterDTO.getCity());
        if (recruiterDTO.getAddressLine1() != null) company.setAddressLine1(recruiterDTO.getAddressLine1());
        if (recruiterDTO.getAddressLine2() != null) company.setAddressLine2(recruiterDTO.getAddressLine2());
        if (recruiterDTO.getPhoneNumber1() != null) company.setPhoneNumber1(recruiterDTO.getPhoneNumber1());
        if (recruiterDTO.getPhoneNumber2() != null) company.setPhoneNumber2(recruiterDTO.getPhoneNumber2());
        if (recruiterDTO.getWebsite() != null) company.setWebsite(recruiterDTO.getWebsite());
        if (recruiterDTO.getField() != null) company.setField(recruiterDTO.getField());
        if (recruiterDTO.getDescription() != null) company.setDescription(recruiterDTO.getDescription());

        companyRepository.save(company);
        return recruiterRepository.save(recruiter);
    }
    @Transactional
    @Override
    public void deleteRecruiter(String recruiterId) {
        logger.info("Starting deletion process for recruiter with ID: {}", recruiterId);

        // Récupérer le recruteur
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        logger.info("Recruiter found: {}", recruiter.getId());

        // Supprimer toutes les offres associées directement via la colonne recruiter_id
        logger.info("Deleting offers for recruiter ID: {}", recruiterId);
        try {
            offerRepository.deleteByRecruiterIdDirect(recruiterId);
            logger.info("Successfully deleted offers for recruiter ID: {}", recruiterId);

            // Vérifier si des offres restent
            List<Offer> remainingOffers = offerRepository.findByRecruiter(recruiter);
            if (!remainingOffers.isEmpty()) {
                logger.error("Offers still exist after deletion attempt: {}", remainingOffers);
                throw new RuntimeException("Failed to delete all offers. Remaining offers: " + remainingOffers.size());
            }
        } catch (Exception e) {
            logger.error("Failed to delete offers: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete associated offers: " + e.getMessage());
        }

        // Supprimer l'utilisateur dans Keycloak
        logger.info("Deleting user in Keycloak for recruiter ID: {}", recruiterId);
        Response response = keycloakAdminService.deleteUser(recruiter.getKeycloakId());
        if (response.getStatus() != 204) {
            logger.error("Keycloak deletion failed with status: {}", response.getStatus());
            throw new RuntimeException("Error deleting user in Keycloak: " + response.getStatus());
        }
        logger.info("Successfully deleted user in Keycloak");

        // Supprimer l'entreprise associée
        Company company = recruiter.getCompany();
        if (company != null) {
            logger.info("Deleting associated company for recruiter ID: {}", recruiterId);
            company.setRecruiter(null);
            companyRepository.delete(company);
            logger.info("Successfully deleted company");
        }

        // Enfin, supprimer le recruteur
        logger.info("Deleting recruiter with ID: {}", recruiterId);
        recruiterRepository.delete(recruiter);
        logger.info("Successfully deleted recruiter");
    }
    @Override
    public Optional<Recruiter> getRecruiterById(String recruiterId) {
        // Vérifier que l'utilisateur est un administrateur (la vérification est faite dans le controller)
        return recruiterRepository.findById(recruiterId);
    }

    @Override
    public List<Recruiter> getAllRecruiters() {
        // Vérifier que l'utilisateur est un administrateur (la vérification est faite dans le controller)
        return recruiterRepository.findAll();
    }
}