package com.demo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Embeddable
public final class UserCredentials {
    public static final String ADMIN_USER_NAME = "admin";

    @NotBlank
    @Column(unique = true, nullable = false)
    private String name;
    private String password;

    protected UserCredentials() {
    }

    public UserCredentials(@NotBlank String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public boolean isAdminUser() {
        return ADMIN_USER_NAME.equals(getName());
    }

}