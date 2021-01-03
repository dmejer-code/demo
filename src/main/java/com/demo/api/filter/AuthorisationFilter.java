package com.demo.api.filter;

import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.service.UserService;
import com.demo.api.util.AuthUtil;
import com.demo.api.model.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Component
public class AuthorisationFilter extends BaseAuthFilter {

    private static final String ROLE_ENDPOINT_PATH_PATTERN = "/roles/**";
    protected static final List<String> EXCLUDE_URL_FROM_AUTHORIZATION = Arrays.asList("/auth/reset");
    private final UserService userService;

    @Autowired
    public AuthorisationFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        UserEntity userEntity = userService.findById(AuthUtil.getSessionAttribute(request, AuthUtil.AUTHENTICATED_USER_ATTR));

        if (!hasAuthority(request, userEntity)) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Not authorised");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean hasAuthority(HttpServletRequest request, UserEntity userEntity) {
        return userEntity != null
                && !userEntity.getRoles().isEmpty()
                && (hasAuthorityToAccessUsers(request, userEntity.getPermissions())
                || hasAuthorityToAccessRoles(request, userEntity.getUserCredentials()));
    }

    private boolean hasAuthorityToAccessUsers(HttpServletRequest request, Set<Permission> permissionSet) {
        return Permission.hasPermission(RequestMethod.valueOf(request.getMethod()), permissionSet, getPathWithinApplication(request));
    }

    private boolean hasAuthorityToAccessRoles(HttpServletRequest request, UserCredentials userCredentials) {
        AntPathMatcher matcher = new AntPathMatcher();
        return userCredentials.isAdminUser()
                && matcher.match(ROLE_ENDPOINT_PATH_PATTERN, getPathWithinApplication(request));
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return super.shouldNotFilter(request)
                || AuthUtil.verifyPathPattern(EXCLUDE_URL_FROM_AUTHORIZATION, getPathWithinApplication(request));
    }

}
