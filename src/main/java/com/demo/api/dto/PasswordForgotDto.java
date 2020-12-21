package com.demo.api.dto;

import javax.validation.constraints.NotBlank;

public class PasswordForgotDto {

    @NotBlank
    private String userName;
    private String token;
    private String newPassword;

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }

}
