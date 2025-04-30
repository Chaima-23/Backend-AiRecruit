package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;

public interface CandidateService {
    Candidate registerCandidate(CandidateDTO candidateDTO, UserDTO userDTO);
}
