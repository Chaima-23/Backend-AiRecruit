package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;

import java.util.List;
import java.util.Optional;

public interface RecruiterService {
    Recruiter registerRecruiter(RecruiterDTO recruiterDTO, UserDTO userDTO);
    Recruiter getCurrentRecruiter();
    Recruiter updateRecruiter(String recruiterId, RecruiterDTO recruiterDTO, UserDTO userDTO);
    void deleteRecruiter(String recruiterId);
    Optional<Recruiter> getRecruiterById(String recruiterId);
    List<Recruiter> getAllRecruiters();
}