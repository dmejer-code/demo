package com.demo.api.dto;

public final class RoleDto {

    private String name;
    private boolean createUser;
    private boolean updateUser;
    private boolean deleteUser;
    private boolean listUser;

    private RoleDto(RoleDtoBuilder roleDtoBuilder) {
        this.name = roleDtoBuilder.name;
        this.createUser = roleDtoBuilder.createUser;
        this.updateUser = roleDtoBuilder.updateUser;
        this.deleteUser = roleDtoBuilder.deleteUser;
        this.listUser = roleDtoBuilder.listUser;
    }

    public String getName() {
        return name;
    }

    public boolean isCreateUser() {
        return createUser;
    }

    public boolean isUpdateUser() {
        return updateUser;
    }

    public boolean isDeleteUser() {
        return deleteUser;
    }

    public boolean isListUser() {
        return listUser;
    }

    public static class RoleDtoBuilder {
        private String name;
        private boolean createUser;
        private boolean updateUser;
        private boolean deleteUser;
        private boolean listUser;

        public RoleDtoBuilder(String name) {
            this.name = name;
        }

        public RoleDtoBuilder createUser(boolean createUser) {
            this.createUser = createUser;
            return this;
        }

        public RoleDtoBuilder updateUser(boolean updateUser) {
            this.updateUser = updateUser;
            return this;
        }

        public RoleDtoBuilder deleteUser(boolean deleteUser) {
            this.deleteUser = deleteUser;
            return this;
        }

        public RoleDtoBuilder listUser(boolean listUser) {
            this.listUser = listUser;
            return this;
        }

        public RoleDto build() {
            return new RoleDto(this);
        }

    }

}
