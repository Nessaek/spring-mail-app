package com.nk.mail.config;


import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Slf4j
@Configuration
public class Config {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    int port;

    @Value("${spring.mail.username}")
    String username;

    @Value("${spring.mail.password}")
    String password;

    @Value("${spring.mail.properties.smtp.auth}")
    String auth;

    @Value("${spring.mail.properties.smtp.starttls.enable}")
    String enableTlS;

    @Value("${spring.mail.properties.smtp.socketFactory.port}")
    String factoryPort;

    @Value("${spring.mail.properties.smtp.socketFactory.class}")
    String factoryClass;

    @Value("${spring.mail.properties.smtp.socketFactory.fallback}")
    String factoryFallback;


    @Bean()
    public JavaMailSender getMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);

        mailSender.setPassword(password);

        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", enableTlS);
        props.put("mail.smtp.socketFactory.class", factoryClass);

        return mailSender;
    }



}
