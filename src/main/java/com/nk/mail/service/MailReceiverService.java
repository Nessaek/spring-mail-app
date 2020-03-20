package com.nk.mail.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.test.mail.TestMailServer;
import org.springframework.stereotype.Service;

@Service
public class MailReceiverService implements CommandLineRunner {

    @Bean
    TestMailServer.ImapServer imapServer() {
        return TestMailServer.imap(0);
    }

    private static Log logger = LogFactory.getLog(MailReceiverService.class);

    public static void manageMessage() {

    @SuppressWarnings("resource")
    ClassPathXmlApplicationContext ac =
        new ClassPathXmlApplicationContext(
            "/integration/gmail-imap-idle-config.xml");
   DirectChannel inputChannel = ac.getBean("receiveChannel", DirectChannel.class);

    		inputChannel.subscribe(message -> {
                org.springframework.messaging.Message<MimeMessage> received =
                    (org.springframework.messaging.Message<MimeMessage>) message;

                try {
                    logger.info("Message: " + received.getPayload().getSubject());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });

    }




    @Override
    public void run(String... args) {
        manageMessage();
    }


}
