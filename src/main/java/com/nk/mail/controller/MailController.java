package com.nk.mail.controller;

import com.nk.mail.model.Email;
import com.nk.mail.service.DialogFlowService;
import com.nk.mail.service.MailSenderService;
import java.util.Collections;
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

    private final DialogFlowService dialogFlowService;

   @PostMapping("/send_email")

    public String sendEmail(@RequestBody Email message) throws Exception {
       mailSenderService.sendSimpleMessage(new Email("nessaek@gmail.com", "my subject", "my text"));
//       dialogFlowService.detectIntentTexts("quick-filament-272623", "my order number is 1234", "fc963089-f42f-4994-b330-968d4e367ea6", "en-US");
       return String.format("sent %1$s to %2$s", message.getSubject(), message.getEmailAddress()) ;



   }

}
