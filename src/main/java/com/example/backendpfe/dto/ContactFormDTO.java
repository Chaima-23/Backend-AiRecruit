package com.example.backendpfe.dto;

import lombok.Data;

@Data
public class ContactFormDTO {
    private String fullName;
    private String email;
    private String phoneNumber;
    private String subject;
    private String message;

}
