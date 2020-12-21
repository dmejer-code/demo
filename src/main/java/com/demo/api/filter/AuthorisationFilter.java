package com.demo.api.filter;

import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.service.UserService;
import com.demo.api.util.AuthUtil;
import com.demo.api.util.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

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
        UserEntity userEntity = userService.findById(AuthUtil.<UUID>getSessionAttribute(request, AuthUtil.AUTHENTICATED_USER_ATTR));
        RoleEntity roleEntity = userEntity.getRoleEntity();

        // @formatter:off
        if (userEntity == null
                || roleEntity == null
                || !(isAuthorisedToAccessUsers(request, roleEntity)
                    || isAuthorisedToAccessRoles(request, userEntity.getUserCredentials()))) {
            response.sendError(HttpStatus.FORBIDDEN.value(), "Not authorised");
            return;
        }
        // @formatter:on

        chain.doFilter(request, response);
    }

    private boolean isAuthorisedToAccessUsers(HttpServletRequest request, RoleEntity roleEntity) {
        return Permission.hasAuthorisation(RequestMethod.valueOf(request.getMethod()), roleEntity, getPathWithinApplication(request));
    }

    private boolean isAuthorisedToAccessRoles(HttpServletRequest request, UserCredentials userCredentials) {
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
