package com.chrisgya.tryout.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Validated
@Data
@ConstructorBinding
@ConfigurationProperties("spring.datasource")
public class CustomDataSourceProperties {
    @NotEmpty
    private final String driverClassName;
    @NotEmpty
    private final String url;
    @NotEmpty
    private final String username;
    @NotEmpty
    private final String password;
}
