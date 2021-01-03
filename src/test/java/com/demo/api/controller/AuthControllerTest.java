package com.demo.api.controller;

import com.demo.api.dto.PasswordForgotDto;
import static com.demo.api.model.UserCredentials.*;

import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.util.AuthUtil;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.servlet.http.HttpSession;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends BaseRestControllerTest {

    private static final String LOGIN_ENDPOINT_PATH = "/auth/login";
    private static final String FORGOT_ENDPOINT_PATH = "/auth/forgot";

    private HttpSession session;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    void login_notAuthenticated() throws Exception {
        // given
        UserCredentials casualUserCredentials = new UserCredentials("user", "test");
        String jsonUserCredentials = asJsonString(casualUserCredentials);

        // when
        MvcResult mvcResult = mockMvc.perform(
                get(LOGIN_ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserCredentials)
        )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print())
                .andReturn();

        // then
        assertNull(mvcResult.getRequest().getSession(false));
    }

    private String forgot_init() throws Exception {
        // given
        PasswordForgotDto passwordForgotDto = new PasswordForgotDto.PasswordForgotDtoBuilder(ADMIN_USER_NAME).build();
        String jsonPasswordForgotDto = asJsonString(passwordForgotDto);

        // when
        MvcResult mvcResult = mockMvc.perform(
                put(FORGOT_ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonPasswordForgotDto)
        )
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andDo(print())
                .andReturn();

        // then
        assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
        session = mvcResult.getRequest().getSession(false);
        assertNotNull(mvcResult.getRequest().getSession(false));
        String token = AuthUtil.getSessionAttribute(mvcResult.getRequest(), AuthUtil.AUTHENTICATION_TOKEN_ATTR);
        assertNotNull(token);

        return token;
    }

    @Test
    @Order(2)
    void forgot() throws Exception {
        // given
        String token = forgot_init();
        PasswordForgotDto passwordForgotDto = new PasswordForgotDto.PasswordForgotDtoBuilder(ADMIN_USER_NAME)
                .withToken(token)
                .withNewPassword(ADMIN_PASSWORD)
                .build();
        String jsonPasswordForgotDto = asJsonString(passwordForgotDto);

        // when
        MvcResult mvcResult = mockMvc.perform(
                put(FORGOT_ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .session(new SessionWrapper(session))
                        .content(jsonPasswordForgotDto)
        )
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print())
                .andReturn();

        // then
        assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
        assertNull(mvcResult.getRequest().getSession(false));
    }

    @Test
    @Order(3)
    void login() throws Exception {
        // given
        UserEntity adminUser = getAdminUser();
        String jsonUserCredentials = asJsonString(getAdminCredentials());

        // when
        MvcResult mvcResult = mockMvc.perform(
                get(LOGIN_ENDPOINT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonUserCredentials)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        // then
        UUID sessionUserId = AuthUtil.getSessionAttribute(mvcResult.getRequest(), AuthUtil.AUTHENTICATED_USER_ATTR);
        assertEquals(adminUser.getId(), sessionUserId);
        assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
    }

}