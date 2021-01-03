package com.demo.api.controller;

import com.demo.api.model.RoleEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest extends BaseRestControllerTest {

    private static final String USER_ROLE_ENDPOINT_PATH = "/roles";

    @Autowired
    private MockMvc mockMvc;
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
    @Transactional(readOnly = true)
    void listRoles() throws Exception {
        // given
        MockHttpSession session = authenticateAsAdminUser();

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
        Set<RoleEntity> roleEntitySetFromDb = getAdminUser().getRoles();
        assertEquals(1, roleEntitySetFromDb.size());

        assertJsonResponse(roleEntityFromJsonResponse, roleEntitySetFromDb.iterator().next());
    }

    private void assertJsonResponse(RoleEntity roleEntityFromJsonResponse, RoleEntity roleEntityFromDb) {
        assertNotNull(roleEntityFromJsonResponse.getId());
        assertEquals(roleEntityFromJsonResponse.getId().toString(), roleEntityFromDb.getId().toString());
        assertEquals(roleEntityFromJsonResponse.getName(), roleEntityFromDb.getName());
        assertThat(roleEntityFromJsonResponse.getPermissions(), is(roleEntityFromDb.getPermissions()));
    }

}