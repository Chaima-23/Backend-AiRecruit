package com.example.backendpfe.service.user.local;

import com.example.backendpfe.service.user.MailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendContactEmail(String name, String email, String phone, String subject, String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("airecruit.assistant@gmail.com");
        message.setSubject("New Contact Message: " + subject);
        message.setText("From: " + name + "\n"
                + "Email: " + email + "\n"
                + "Phone: " + phone + "\n\n"
                + "Message:\n" + messageContent);
        mailSender.send(message);
    }
}
