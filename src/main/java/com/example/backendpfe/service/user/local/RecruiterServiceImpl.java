package com.example.backendpfe.service.user.local;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.profile.Company;
import com.example.backendpfe.service.user.RecruiterService;
import com.example.backendpfe.service.user.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

    private static final Logger logger = LoggerFactory.getLogger(RecruiterServiceImpl.class);

    private final RecruiterRepository recruiterRepository;
    private final CompanyRepository companyRepository;
    private final KeycloakAdminService keycloakAdminService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Recruiter registerRecruiter(RecruiterDTO recruiterDTO, UserDTO userDTO) {

        if (recruiterRepository.existsByUsername(userDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (recruiterRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Créer profil company
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

        // Créer l'utilisateur dans Keycloak
        String keycloakId = keycloakAdminService.createUserAndGetId(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());

        // Créer Recruiter
        Recruiter recruiter = new Recruiter();
        recruiter.setUsername(userDTO.getUsername());
        recruiter.setEmail(userDTO.getEmail());
        recruiter.setPassword(passwordEncoder.encode(userDTO.getPassword()));
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
            throw new IllegalStateException("Email not found in token");
        }
        logger.info("Looking for recruiter with email: {}", email);
        return recruiterRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
    }

    private String extractEmailFromToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            Jwt jwt = (Jwt) authentication.getPrincipal();
            return jwt.getClaim("email");
        }
        return null;
    }
    }
