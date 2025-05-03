package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;
import com.example.backendpfe.models.profile.Company;
import com.example.backendpfe.service.CompanyRepository;
import com.example.backendpfe.repository.user.RecruiterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecruiterServiceImpl implements RecruiterService {

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
}
