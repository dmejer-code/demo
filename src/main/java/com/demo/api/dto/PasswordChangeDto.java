package com.demo.api.dto;

import javax.validation.constraints.NotBlank;

public final class PasswordChangeDto {

    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public boolean isOfSameValue() {
        return oldPassword.equals(newPassword);
    }

}