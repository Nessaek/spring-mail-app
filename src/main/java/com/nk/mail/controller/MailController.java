package com.nk.mail.controller;

import com.nk.mail.model.Email;
import com.nk.mail.service.MailSenderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping()
public class MailController {


    private final MailSenderService mailSenderService;

   @PostMapping("/send_email")

    public String sendEmail(@RequestBody Email message) {
       mailSenderService.sendSimpleMessage(message);
       return String.format("sent %1$s to %2$s", message.getSubject(), message.getEmailAddress()) ;



   }

}
