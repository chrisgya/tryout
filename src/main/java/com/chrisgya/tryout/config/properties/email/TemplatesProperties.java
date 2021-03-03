package com.chrisgya.tryout.config.properties.email;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class TemplatesProperties {
    @NotEmpty
    private final String userDeactivation;
    @NotEmpty
    private final String userVerification;
}
