package com.softserve.itacademy.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    Long id;
    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @NotBlank
    String login;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String role;
}