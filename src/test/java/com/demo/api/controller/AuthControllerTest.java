package com.demo.api.controller;

import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.util.AuthUtil;
import com.demo.api.util.BaseRestControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest extends BaseRestControllerTest {

    private static final String LOGIN_ENDPOINT_PATH = "/auth/login";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login() throws Exception {
        // given
        UserEntity adminUser = getAdminUser();
        String jsonUserCredentials = asJsonString(adminUser.getUserCredentials());

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
        UUID sessionUserId = AuthUtil.<UUID>getSessionAttribute(mvcResult.getRequest(), AuthUtil.AUTHENTICATED_USER_ATTR);
        assertEquals(adminUser.getId(), sessionUserId);
        assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
    }

    @Test
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

    private String asJsonString(final Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}