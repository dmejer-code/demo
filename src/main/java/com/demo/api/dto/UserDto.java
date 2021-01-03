package com.demo.api.dto;

import com.demo.api.model.UserCredentials;

import java.util.HashSet;
import java.util.Set;

public final class UserDto {

    private String name;
    private String password;
    private final Set<String> roleNames = new HashSet<>();

    public UserDto() {
    }

    private UserDto(UserDtoBuilder userDtoBuilder) {
        this.name = userDtoBuilder.name;
        this.password = userDtoBuilder.password;
        this.roleNames.addAll(userDtoBuilder.roleNames);
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoleNames() {
        return roleNames;
    }

    public UserCredentials getUserCredentials() {
        return new UserCredentials(name, password);
    }

    public static class UserDtoBuilder {
        private final String name;
        private final String password;
        private final Set<String> roleNames = new HashSet<>();

        public UserDtoBuilder(UserCredentials userCredentials) {
            this.name=userCredentials.getName();
            this.password=userCredentials.getPassword();
        }

        public UserDtoBuilder withRoleName(String roleName) {
            this.roleNames.add(roleName);
            return this;
        }

        public UserDtoBuilder withRoleNames(Set<String> roleNames) {
            this.roleNames.addAll(roleNames);
            return this;
        }

        public UserDto build() {
            return new UserDto(this);
        }
    }
}
