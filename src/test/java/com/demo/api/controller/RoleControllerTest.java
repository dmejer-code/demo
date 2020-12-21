package com.demo.api.controller;

import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.service.UserService;
import com.demo.api.util.AuthUtil;
import com.demo.api.util.BaseRestControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest extends BaseRestControllerTest {

    private static final String USER_ROLE_ENDPOINT_PATH = "/roles";

    private MockHttpSession session;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listRoles_unauthorised() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get(USER_ROLE_ENDPOINT_PATH))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(print())
                .andReturn();

        // then
       assertTrue(mvcResult.getResponse().getContentAsString().isBlank());
    }

    @Test
    void listRoles() throws Exception {
        // given
        authenticateAsAdminUser();

        // when
        MvcResult mvcResult = mockMvc.perform(
                get(USER_ROLE_ENDPOINT_PATH).session(session)
        )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andReturn();

        // then
        List<RoleEntity> roleList = Arrays.asList(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), RoleEntity[].class));
        assertEquals(1, roleList.size());

        RoleEntity roleEntityFromJsonResponse = roleList.get(0);
        RoleEntity roleEntityFromDb = getAdminUser().getRoleEntity();
        assertJsonResponse(roleEntityFromJsonResponse, roleEntityFromDb);
    }

    private void assertJsonResponse(RoleEntity roleEntityFromJsonResponse, RoleEntity roleEntityFromDb) {
        assertNotNull(roleEntityFromJsonResponse.getId());
        assertEquals(roleEntityFromJsonResponse.getId().toString(), roleEntityFromDb.getId().toString());
        assertEquals(roleEntityFromJsonResponse.getName(), roleEntityFromDb.getName());
        assertEquals(roleEntityFromJsonResponse.isListUser(), roleEntityFromDb.isListUser());
        assertEquals(roleEntityFromJsonResponse.isCreateUser(), roleEntityFromDb.isCreateUser());
        assertEquals(roleEntityFromJsonResponse.isDeleteUser(), roleEntityFromDb.isDeleteUser());
        assertEquals(roleEntityFromJsonResponse.isUpdateUser(), roleEntityFromDb.isUpdateUser());
    }

    private void authenticateAsAdminUser() {
        session = new MockHttpSession();
        session.setAttribute(AuthUtil.AUTHENTICATED_USER_ATTR, getAdminUser().getId());
    }

}