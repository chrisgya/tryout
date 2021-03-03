package com.chrisgya.tryout.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Validated
@Data
@ConstructorBinding
@ConfigurationProperties("app.security")
public class AppSecurityProperties {
    @NotNull
    private final Integer passwordStrength;
}
