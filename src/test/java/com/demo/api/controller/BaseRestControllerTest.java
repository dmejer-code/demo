package com.demo.api.controller;

import static com.demo.api.model.UserCredentials.*;

import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.service.UserService;
import com.demo.api.util.AuthUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;

public abstract class BaseRestControllerTest {

    protected static final String ADMIN_PASSWORD = "password";

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    protected UserEntity getAdminUser() {
        return userService.findByUserName(UserCredentials.ADMIN_USER_NAME);
    }

    protected String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected MockHttpSession authenticateAsAdminUser() {
        MockHttpSession session = new MockHttpSession();
        session.setAttribute(AuthUtil.AUTHENTICATED_USER_ATTR, getAdminUser().getId());
        return session;
    }

    protected UserCredentials getAdminCredentials() {
        return new UserCredentials(ADMIN_USER_NAME, ADMIN_PASSWORD);
    }

    public static class SessionWrapper extends MockHttpSession{
        private final HttpSession httpSession;

        public SessionWrapper(HttpSession httpSession){
            this.httpSession = httpSession;
        }

        @Override
        public Object getAttribute(String name) {
            return this.httpSession.getAttribute(name);
        }
    }

}
