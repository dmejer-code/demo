package com.demo.api.model;

import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.web.bind.annotation.RequestMethod.*;

public enum Permission {
    CREATE_USER(POST,"/users/**"),
    UPDATE_USER(PUT,"/users/**"),
    DELETE_USER(DELETE,"/users/**"),
    LIST_USER(GET,"/users/**");

    private final RequestMethod httpMethod;
    private final String pathPattern;

    Permission(RequestMethod httpMethod, String pathPattern) {
        this.httpMethod = httpMethod;
        this.pathPattern = pathPattern;
    }

    public static boolean hasPermission(RequestMethod httpMethod, Set<Permission> permissionSet, String serverPath) {
        AntPathMatcher matcher = new AntPathMatcher();

            return Stream.of(Permission.values())
                    .filter(permission -> matcher.match(permission.pathPattern, serverPath))
                    .filter(permission -> httpMethod.equals(permission.httpMethod))
                    .anyMatch(permissionSet::contains);
    }

}
