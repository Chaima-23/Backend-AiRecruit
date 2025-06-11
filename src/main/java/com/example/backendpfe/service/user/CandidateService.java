package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.CandidateDTO;
import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Candidate;


import java.util.List;
import java.util.Optional;

public interface CandidateService {
        Candidate registerCandidate(CandidateDTO candidateDTO, UserDTO userDTO);
        void updateCandidate(String candidateId, CandidateDTO candidateDTO, UserDTO userDTO);
        void deleteCandidate(String candidateId);
        List<Candidate> getAllCandidates();
        Optional<Candidate> getCandidateById(String candidateId);
}
