package com.example.backendpfe.service.user;

import com.example.backendpfe.dto.UserDTO;
import com.example.backendpfe.models.idm.Administrator;

public interface AdminService {
    Administrator createAdmin(UserDTO userDTO);
    Administrator updateOwnAdmin(UserDTO userDTO);
}
