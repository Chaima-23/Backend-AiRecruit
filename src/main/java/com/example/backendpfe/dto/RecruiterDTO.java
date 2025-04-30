package com.example.backendpfe.dto;

import lombok.Data;

@Data
public class RecruiterDTO {
    private String companyName;
    private int companySize;
    private String country;
    private String city;
    private String addressLine1;
    private String addressLine2;
    private String phoneNumber1;
    private String phoneNumber2;
    private String website;
    private String field;
    private String description;
}
