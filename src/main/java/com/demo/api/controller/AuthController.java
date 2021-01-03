package com.demo.api.controller;

import com.demo.api.dto.PasswordChangeDto;
import com.demo.api.dto.PasswordForgotDto;
import com.demo.api.model.UserCredentials;
import com.demo.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;

@RestController
@RequestMapping("auth")
@EnableAutoConfiguration
public class AuthController {

    private final AuthService authService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Autowired
    public AuthController(AuthService authService, HttpServletRequest request, HttpServletResponse response) {
        this.authService = authService;
        this.request = request;
        this.response = response;
    }

    @RequestMapping("login")
    public void login(@RequestBody @Valid @NotNull UserCredentials credentials) {
        authService.login(request, credentials);
    }

    @PutMapping("reset")
    public void reset(@RequestBody @Valid @NotNull PasswordChangeDto password) {
        if (!authService.resetPassword(request, password)) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Failed to update password");
        }
    }

    @PutMapping("forgot")
    public void forgot(@RequestBody @Valid @NotNull PasswordForgotDto passwordForgotDto) throws IOException {
        authService.recoverPassword(request, response, passwordForgotDto);
    }

}
