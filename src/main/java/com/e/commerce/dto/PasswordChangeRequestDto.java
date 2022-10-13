package com.e.commerce.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PasswordChangeRequestDto {

    @NotBlank
    private String oldPassword;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String newPasswordRepeat;
}
