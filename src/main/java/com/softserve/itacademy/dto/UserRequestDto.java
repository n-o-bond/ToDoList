package com.softserve.itacademy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Data
public class UserRequestDto {

    @NotBlank
    private String login;

    @NotBlank
    private String password;
}
