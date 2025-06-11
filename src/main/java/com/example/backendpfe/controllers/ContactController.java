package com.example.backendpfe.controllers;

import com.example.backendpfe.dto.ContactFormDTO;
import com.example.backendpfe.service.user.MailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
@CrossOrigin("*")
public class ContactController {

    private final MailService mailService;

    public ContactController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<?> sendContactMessage(@RequestBody ContactFormDTO contactForm) {
        mailService.sendContactEmail(
                contactForm.getFullName(),
                contactForm.getEmail(),
                contactForm.getPhoneNumber(),
                contactForm.getSubject(),
                contactForm.getMessage()
        );
        return ResponseEntity.ok().body(Map.of("message", "Message sent successfully"));
    }
}
