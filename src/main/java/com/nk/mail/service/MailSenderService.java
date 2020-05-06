package com.nk.mail.service;

import com.nk.mail.model.Email;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MailSenderService  {


    public JavaMailSender emailSender;


    public void sendSimpleMessage(
        Email mail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getEmailAddress());
        message.setSubject(mail.getSubject());
        message.setText(mail.getMessage());
        emailSender.send(message);
    }

}
