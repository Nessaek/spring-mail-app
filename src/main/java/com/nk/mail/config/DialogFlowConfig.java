package com.nk.mail.config;

import com.nk.mail.service.DialogFlowService;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "dialog-flow")
@Setter
@Getter
public class DialogFlowConfig {

    @NotNull
    private String projectId;

    @NotNull
    private String sessionId;

    @NotNull
    private String languageCode;
}
