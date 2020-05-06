package com.nk.mail.service;

import com.google.cloud.dialogflow.v2.QueryResult;
import com.nk.mail.model.Email;
import com.sun.tools.javac.util.Convert;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.Multipart;
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


    DialogFlowService dialogFlowService;



    public void manageMessage() {

    @SuppressWarnings("resource")
    ClassPathXmlApplicationContext ac =
        new ClassPathXmlApplicationContext(
            "/integration/gmail-imap-idle-config.xml");
   DirectChannel inputChannel = ac.getBean("receiveChannel", DirectChannel.class);

    		inputChannel.subscribe(message -> {


                try {
                   String messageBody = getMessageBody(message);

                  String cleanedBody = cleanseBody(messageBody);
                 List<String> sentences = convertContentToList(cleanedBody);

                 Map<String, QueryResult> responses =   dialogFlowService.detectIntentTexts(sentences);


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

    });

    }

    String getMessageBody(Message<?> message)
        throws IOException, MessagingException {

        return ((Multipart) ((javax.mail.internet.MimeMessage) message.getPayload()).getContent()).getBodyPart(0).getContent().toString();

    }

    String cleanseBody(String messageBody) {

        return messageBody.replaceAll("\\<.*?\\>|\\r?\\n|\\r/", "");
    }

    public static List<String> convertContentToList(String content) {


        return Arrays.asList(content.split("[\\p{Punct}]+"));

    }




    @Override
    public void run(String... args) {
        manageMessage();
    }


}
