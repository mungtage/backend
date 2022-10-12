package com.example.mungtage.domain.User.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CreateUserRequestDto {
    @Email
    @NotBlank
    private String email;
}
