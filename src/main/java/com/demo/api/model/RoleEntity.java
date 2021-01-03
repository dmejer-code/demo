package com.demo.api.model;

import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import java.util.*;

@Entity(name = "role")
public final class RoleEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type = "uuid-char")
    @Column(unique = true, updatable = false, nullable = false)
    private UUID id;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String name;

    @ElementCollection(targetClass = Permission.class)
    @CollectionTable(name = "role_permission",
            joinColumns = @JoinColumn(name = "role_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    @BatchSize(size = 10)
    private final Set<Permission> permissions = new HashSet<>();

    public RoleEntity() {
    }

    private RoleEntity(RoleEntityBuilder roleEntityBuilder) {
        this.name = roleEntityBuilder.roleName;
        this.permissions.addAll(roleEntityBuilder.permissions);
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    @Override
    public String toString() {
        return "RoleEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", permissionSet=" + permissions +
                '}';
    }

    public static class RoleEntityBuilder {
        private final String roleName;
        private final Set<Permission> permissions = new HashSet<>();

        public RoleEntityBuilder(String roleName) {
            this.roleName = roleName;
        }

        public RoleEntityBuilder withPermission(Permission permission) {
            if (permission != null) {
                this.permissions.add(permission);
            }

            return this;
        }

        public RoleEntityBuilder withPermissions(Set<Permission> permissionSet) {
            this.permissions.addAll(permissionSet);
            return this;
        }

        public RoleEntity build() {
            return new RoleEntity(this);
        }
    }

}
