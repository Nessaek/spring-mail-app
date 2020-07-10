package com.nk.mail.service;

import com.nk.mail.model.Email;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ResponseService {

    MailSenderService mailSenderService;

    void manageReply(Email receivedEmail) {
        mailSenderService.sendSimpleMessage(new Email(receivedEmail.getEmailAddress(), "re: " + receivedEmail.getSubject(), "Thank you for your email"));
    }


}
