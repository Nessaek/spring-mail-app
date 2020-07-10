package com.nk.mail.service;

import com.nk.mail.config.Config;
import com.nk.mail.model.Email;
import java.io.IOException;
import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Slf4j
@Service
public class MailReceiverService implements CommandLineRunner {

   ResponseService responseService;



    public void manageMessage() {

    @SuppressWarnings("resource")
    ClassPathXmlApplicationContext ac =
        new ClassPathXmlApplicationContext(
            "/integration/gmail-imap-idle-config.xml");
   DirectChannel inputChannel = ac.getBean("receiveChannel", DirectChannel.class);

    		inputChannel.subscribe(message -> {


                try {
                   Email email = getEmail(message);


                   if(email.getEmailAddress().equals(new Config().getUsername())) {
                       responseService.manageReply(email);

                   }
                } catch (IOException | MessagingException e) {
                    e.printStackTrace();
                }

            });

    }

    Email getEmail(Message<?> message) throws MessagingException, IOException {

        MimeMessage mimeMessage = ((MimeMessage) message.getPayload());

       Address[] addresses =  mimeMessage.getFrom();

        String emailAddress = addresses == null ? null : ((InternetAddress) addresses[0]).getAddress();

        String subject = mimeMessage.getSubject();

        String messageBody = ((Multipart) mimeMessage.getContent()).getBodyPart(0).getContent().toString();

        return new Email(emailAddress, subject, messageBody);


    }


    @Override
    public void run(String... args) {
        manageMessage();
    }


}
