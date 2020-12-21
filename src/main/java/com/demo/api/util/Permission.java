package com.demo.api.util;

import com.demo.api.model.RoleEntity;
import org.apache.logging.log4j.util.Strings;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public enum Permission {
    CREATE_USER(POST, RoleEntity::isCreateUser),
    UPDATE_USER(PUT, RoleEntity::isUpdateUser),
    DELETE_USER(DELETE, RoleEntity::isDeleteUser),
    LIST_USER(GET, RoleEntity::isListUser);

    private static final String USER_ENDPOINT_PATH_PATTERN = "/users/**";
    private final RequestMethod httpMethod;
    private final Predicate<RoleEntity> permissionPredicate;

    private Permission(RequestMethod httpMethod, Predicate<RoleEntity> permissionPredicate) {
        this.httpMethod = httpMethod;
        this.permissionPredicate = permissionPredicate;
    }

    public static boolean hasAuthorisation(RequestMethod httpMethod, RoleEntity roleEntity, String serverPath) {
        if (Strings.isBlank(serverPath)
                || !new AntPathMatcher().match(USER_ENDPOINT_PATH_PATTERN, serverPath)) {
            return false;
        }

        return Stream.of(Permission.values())
                .filter(p -> httpMethod.equals(p.httpMethod))
                .anyMatch(p -> p.permissionPredicate.test(roleEntity));
    }

}
