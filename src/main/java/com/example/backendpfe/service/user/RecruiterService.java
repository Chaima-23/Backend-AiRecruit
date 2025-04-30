package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.RecruiterDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Recruiter;

public interface RecruiterService {
    Recruiter registerRecruiter(RecruiterDTO recruiterDTO, UserDTO userDTO);
}
