package com.chrisgya.tryout.model.request;

import com.chrisgya.tryout.model.RoleEnum;
import com.chrisgya.tryout.util.validation.ValueOfEnum;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
@Data
public class UpdateUserRequest {
    @JsonIgnore
    private Long id;

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

    @Schema(description = "mobile", example = "+2347087760744", required = true)
    @NotBlank(message = "required")
    @Size(max = 16)
    private String mobile;

    @Schema(example = "USER", required = true)
    @NotBlank(message = "required")
    @ValueOfEnum(enumClass = RoleEnum.class, message = "must be either 'USER' or 'ADMIN'")
    private String role;

}
