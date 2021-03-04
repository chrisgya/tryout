package com.chrisgya.tryout.model.request;

import com.chrisgya.tryout.model.RoleEnum;
import com.chrisgya.tryout.util.validation.Phone;
import com.chrisgya.tryout.util.validation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
public class CreateUserRequest {
    @Schema(description = "title", example = "Mr", required = true)
    @NotBlank(message = "required")
    @Size(min = 2, max = 20)
    private String title;

    @Schema(description = "first name", example = "John", required = true)
    @NotBlank(message = "required")
    @Size(min = 2, max = 50)
    private String firstName;

    @Schema(description = "last name", example = "Doe", required = true)
    @NotBlank(message = "required")
    @Size(min = 2, max = 50)
    private String lastName;

    @Schema(description = "email address", example = "john.doe@yahoo.com", required = true)
    @NotBlank(message = "required")
    @Size(max = 75)
    @Email
    private String email;

    @Schema(description = "mobile", example = "+2347087760744", required = true)
    @NotBlank(message = "required")
    @Phone
    private String mobile;

    @NotBlank(message = "required")
    @Size(min = 4, max = 30)
    private String password;

    @Schema(example = "USER", required = true)
    @NotBlank(message = "required")
    @ValueOfEnum(enumClass = RoleEnum.class, message = "must be either 'USER' or 'ADMIN'")
    private String role;

    @JsonIgnore
    private String verificationCode;
}
