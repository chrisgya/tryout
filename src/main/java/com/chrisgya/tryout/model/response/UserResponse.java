package com.chrisgya.tryout.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;
    private String role;
    private Date dateRegistered;
    private Boolean verified;
    private Date dateVerified;
    private Date dateDeactivated;
    private String status;
}
