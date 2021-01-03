package com.demo.api.dto;

import com.demo.api.model.Permission;

import java.util.HashSet;
import java.util.Set;

public final class RoleDto {

    private String name;
    private Set<Permission> permissions;

    public RoleDto() {
    }

    private RoleDto(RoleDtoBuilder roleDtoBuilder) {
        this.name = roleDtoBuilder.name;
        this.permissions = new HashSet<>(roleDtoBuilder.permissions);
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public static class RoleDtoBuilder {
        private final String name;
        private final Set<Permission> permissions;

        public RoleDtoBuilder(String name) {
            this.name = name;
            this.permissions = new HashSet<>();
        }

        public RoleDtoBuilder withPermission(Permission permission) {
            if (permission != null) {
                this.permissions.add(permission);
            }

            return this;
        }

        public RoleDtoBuilder withPermissions(Set<Permission> permissions) {
            this.permissions.addAll(permissions);
            return this;
        }

        public RoleDto build() {
            return new RoleDto(this);
        }
    }

}
