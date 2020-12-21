package com.demo.api.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "user")
public final class UserEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @Embedded
    private UserCredentials userCredentials;

    @OneToOne
    private RoleEntity roleEntity;

    public UserEntity() {
    }

    private UserEntity(UserEntityBuilder userEntityBuilder) {
        this.userCredentials = userEntityBuilder.userCredentials;
        this.roleEntity = userEntityBuilder.roleEntity;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    public RoleEntity getRoleEntity() {
        return roleEntity;
    }

    public String getRoleName() {
        return roleEntity == null ? null : roleEntity.getName();
    }

    public static class UserEntityBuilder {
        private final UserCredentials userCredentials;
        private RoleEntity roleEntity;

        public UserEntityBuilder(UserCredentials userCredentials) {
            this.userCredentials = userCredentials;
        }

        public UserEntityBuilder roleEntity(RoleEntity roleEntity) {
            this.roleEntity = roleEntity;
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }

    }

}
