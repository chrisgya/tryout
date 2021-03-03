package com.chrisgya.tryout.model.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class MessageUserVerificationRequest implements Serializable {
    private String name;
    private String email;
    private String verificationToken;
}
