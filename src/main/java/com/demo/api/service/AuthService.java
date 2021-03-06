package com.demo.api.service;

import com.demo.api.dto.PasswordChangeDto;
import com.demo.api.dto.PasswordForgotDto;
import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.util.AuthUtil;
import org.hibernate.internal.util.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@Service
public class AuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public void login(HttpServletRequest request, UserCredentials credentials) {
        UserEntity authenticatedUser = userService.getAuthenticatedUser(credentials)
                .orElseThrow(() -> {
                    request.getSession().invalidate();
                    return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user or password");
                });

        request.getSession().setAttribute(AuthUtil.AUTHENTICATED_USER_ATTR, authenticatedUser.getId());
    }

    public boolean resetPassword(HttpServletRequest request, PasswordChangeDto password) {
        UUID authenticatedUserId = AuthUtil.getSessionAttribute(request, AuthUtil.AUTHENTICATED_USER_ATTR);
        return userService.changePassword(authenticatedUserId, password);
    }

    @Transactional
    public void recoverPassword(HttpServletRequest request, HttpServletResponse response, PasswordForgotDto passwordForgotDto) throws IOException {
        UserEntity userEntity = userService.findByUserName(passwordForgotDto.getUserName());

        if (userEntity == null) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "User does not exist");
            return;
        }

        String sessionToken = AuthUtil.getSessionAttribute(request, AuthUtil.AUTHENTICATION_TOKEN_ATTR);

        if (sessionToken == null) {
            setSessionToken(request, response);
            return;
        }

        if (sessionToken.equals(passwordForgotDto.getToken()) && StringHelper.isNotEmpty(passwordForgotDto.getNewPassword())) {
            setNewPassword(request, response, passwordForgotDto, userEntity);
            return;
        }

        request.getSession().invalidate();
        response.sendError(HttpStatus.BAD_REQUEST.value(), "Password recovery failed");
    }

    private void setSessionToken(HttpServletRequest request, HttpServletResponse response) {
        String token = AuthUtil.generateToken();
        request.getSession().setAttribute(AuthUtil.AUTHENTICATION_TOKEN_ATTR, token);
        LOGGER.info("New session token {} has been set!", token);
        response.setStatus(HttpStatus.ACCEPTED.value());
    }

    private void setNewPassword(HttpServletRequest request, HttpServletResponse response, PasswordForgotDto passwordForgotDto, UserEntity userEntity) throws IOException {
        if (!userService.changePassword(userEntity.getId(), passwordForgotDto.getNewPassword())) {
            response.sendError(HttpStatus.BAD_REQUEST.value(), "Could not change password");
            request.getSession().invalidate();
            return;
        }

        LOGGER.info("New password has been set!");
        request.getSession().invalidate();
        response.setStatus(HttpStatus.NO_CONTENT.value());
    }

}
