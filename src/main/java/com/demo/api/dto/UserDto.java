package com.demo.api.dto;

import com.demo.api.model.UserCredentials;

public final class UserDto {

    private String name;
    private String password;
    private String roleName;

    private UserDto(UserDtoBuilder userDtoBuilder) {
        this.name = userDtoBuilder.name;
        this.password = userDtoBuilder.password;
        this.roleName = userDtoBuilder.roleName;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getRoleName() {
        return roleName;
    }

    public UserCredentials getUserCredentials() {
        return new UserCredentials(name, password);
    }

    public static class UserDtoBuilder {
        private String name;
        private String password;
        private String roleName;

        public UserDtoBuilder(UserCredentials userCredentials) {
            this.name=userCredentials.getName();
            this.password=userCredentials.getPassword();
        }

        public UserDtoBuilder roleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }

    }
}
