package com.demo.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @BatchSize(size = 10)
    private final Set<RoleEntity> roles = new HashSet<>();

    public UserEntity() {
    }

    private UserEntity(UserEntityBuilder userEntityBuilder) {
        this.userCredentials = userEntityBuilder.userCredentials;
        this.roles.addAll(userEntityBuilder.roles);
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public UserCredentials getUserCredentials() {
        return userCredentials;
    }

    @JsonIgnore
    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public String getName() {
        return userCredentials.getName();
    }

    public Set<Permission> getPermissions() {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .collect(Collectors.toSet());
    }

    public Set<String> getRoleNames() {
        return roles.stream()
                .map(RoleEntity::getName)
                .collect(Collectors.toSet());
    }

    public static class UserEntityBuilder {
        private final UserCredentials userCredentials;
        private final Set<RoleEntity> roles = new HashSet<>();

        public UserEntityBuilder(UserCredentials userCredentials) {
            this.userCredentials = userCredentials;
        }

        public UserEntityBuilder withRole(RoleEntity roleEntity) {
            if (roleEntity != null) {
                this.roles.add(roleEntity);
            }

            return this;
        }

        public UserEntityBuilder withRoles(Set<RoleEntity> roles) {
            this.roles.addAll(roles);
            return this;
        }

        public UserEntity build() {
            return new UserEntity(this);
        }
    }

}
