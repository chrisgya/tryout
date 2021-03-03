package com.chrisgya.tryout.config.properties.email;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class DefaultsProperties {
    @NotEmpty
    private final String sender;
}
