package com.example.backendpfe.service.user;

public interface MailService {
    void sendContactEmail(String name, String email, String phone, String subject, String messageContent);
}
