package com.nk.mail.model;

import lombok.Data;

@Data
public class Email {

    private String emailAddress;

    private String subject;

    private String message;

}
