package com.nk.mail;

import com.nk.mail.config.DialogFlowConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@Slf4j
@EnableConfigurationProperties(DialogFlowConfig.class)
@SpringBootApplication
public class MailApplication {

	public static void main(String[] args) {

        SpringApplication.run(MailApplication.class, args);

	}




}
