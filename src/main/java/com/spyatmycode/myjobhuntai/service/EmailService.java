
package com.spyatmycode.myjobhuntai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}") // Inject your own email as the sender
    private String senderEmail;
    
    // The "Admin" email who receives the alerts (e.g., You)
    @Value("${admin.notification.email:akejunifemi11@gmail.com}") 
    private String adminEmail;

    /**
     * Async method to send alerts without blocking the API response
     */
    @Async
    public void sendLoginNotification(String userEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(adminEmail); // Or send to the user: message.setTo(userEmail);
            message.setSubject("Security Alert: New Login Detected");
            message.setText("A user just logged in: " + userEmail);

            mailSender.send(message);
            System.out.println("Login notification sent for: " + userEmail);
        } catch (Exception e) {
            // Log error but don't crash the app
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    @Async
    public void sendWelcomeEmail(String userEmail) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderEmail);
            message.setTo(userEmail);
            message.setSubject("Welcome to JobHuntAI!");
            message.setText("Hello! Your account has been successfully created. Happy hunting!");

            mailSender.send(message);
            
            // Optional: Also notify admin
            sendAdminSignupAlert(userEmail);
            
        } catch (Exception e) {
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }
    }
    
    private void sendAdminSignupAlert(String userEmail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(adminEmail);
        message.setSubject("New User Signup!");
        message.setText("New candidate registered: " + userEmail);
        mailSender.send(message);
    }
} 
