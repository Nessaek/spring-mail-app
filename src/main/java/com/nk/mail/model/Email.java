package com.nk.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
public class Email {

    private String emailAddress;

    private String subject;

    private String message;

}
