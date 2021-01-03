package com.demo.api.dto;

import javax.validation.constraints.NotBlank;

public final class PasswordForgotDto {

    @NotBlank
    private String userName;
    private String token;
    private String newPassword;

    public PasswordForgotDto() {
    }

    public PasswordForgotDto(PasswordForgotDtoBuilder passwordForgotDtoBuilder) {
        this.userName = passwordForgotDtoBuilder.userName;
        this.token = passwordForgotDtoBuilder.token;
        this.newPassword = passwordForgotDtoBuilder.newPassword;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public static class PasswordForgotDtoBuilder {
        private final String userName;
        private String token;
        private String newPassword;

        public PasswordForgotDtoBuilder(String userName) {
            this.userName = userName;
        }

        public PasswordForgotDtoBuilder withToken(String token) {
            this.token = token;
            return this;
        }

        public PasswordForgotDtoBuilder withNewPassword(String newPassword) {
            this.newPassword = newPassword;
            return this;
        }

        public PasswordForgotDto build() {
            return new PasswordForgotDto(this);
        }
    }

}
