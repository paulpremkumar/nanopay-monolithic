package com.nanopay.payeasy.notification.controller;

import com.nanopay.payeasy.notification.DTO.EmailTemplateRequest;
import com.nanopay.payeasy.notification.service.EmailNotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class EmailController {

    private final EmailNotificationService emailService;

    public EmailController(EmailNotificationService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email")
    public ResponseEntity<String> sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body) {

        emailService.sendPlainTextEmail(to, subject, body);
        return ResponseEntity.ok("Email sent successfully");
    }
    @PostMapping("/email-welcome")
    public ResponseEntity<String> sendWelcomeMail(@RequestBody EmailTemplateRequest req){
       emailService.sendWelcomeEmail(req);
       return ResponseEntity.ok("Email sent successfully");
    }
}

