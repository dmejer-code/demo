package com.demo.api.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Entity(name = "role")
public final class RoleEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="uuid-char")
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    private boolean createUser;
    private boolean updateUser;
    private boolean deleteUser;
    private boolean listUser;

    public RoleEntity() {
    }

    public RoleEntity(@NotBlank String roleName) {
        this.name = roleName;
    }

    private RoleEntity(RoleEntityBuilder roleEntityBuilder) {
        this.name = roleEntityBuilder.roleName;
        this.createUser = roleEntityBuilder.createUser;
        this.updateUser = roleEntityBuilder.updateUser;
        this.deleteUser = roleEntityBuilder.deleteUser;
        this.listUser = roleEntityBuilder.listUser;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
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

    public static class RoleEntityBuilder {
        private final String roleName;
        private boolean createUser;
        private boolean updateUser;
        private boolean deleteUser;
        private boolean listUser;

        public RoleEntityBuilder(String roleName) {
            this.roleName = roleName;
        }

        public RoleEntityBuilder createUser(boolean createUser) {
            this.createUser = createUser;
            return this;
        }

        public RoleEntityBuilder updateUser(boolean updateUser) {
            this.updateUser = updateUser;
            return this;
        }

        public RoleEntityBuilder deleteUser(boolean deleteUser) {
            this.deleteUser = deleteUser;
            return this;
        }

        public RoleEntityBuilder listUser(boolean listUser) {
            this.listUser = listUser;
            return this;
        }

        public RoleEntity build() {
            return new RoleEntity(this);
        }

    }

}
