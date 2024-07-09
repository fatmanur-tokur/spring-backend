package com.fatmanurtokur.shopping.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;


    public void sendEmail(String to, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("example@gmail.com");
        message.setTo(to);
        message.setSubject("Password Reset Verification Code");
        message.setText("Your verification code is: " + verificationCode);
        mailSender.send(message);
    }
}
