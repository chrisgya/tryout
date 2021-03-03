package com.chrisgya.tryout.config.properties.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Data
@ConstructorBinding
@ConfigurationProperties("email")
public class EmailProperties {
    @Valid
    private final DefaultsProperties defaults;
    @Valid
    private final SubjectProperties subject;
    @Valid
    private final TemplatesProperties templates;
}
