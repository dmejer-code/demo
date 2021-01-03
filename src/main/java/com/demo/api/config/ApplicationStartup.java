package com.demo.api.config;

import static com.demo.api.model.UserCredentials.*;

import com.demo.api.dto.RoleDto;
import com.demo.api.dto.UserDto;
import static com.demo.api.model.Permission.*;
import com.demo.api.model.UserCredentials;
import com.demo.api.service.RoleService;
import com.demo.api.service.UserService;
import com.demo.api.util.AuthUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStartup.class);
    private static final String SUPER_USER_ROLE_NAME = "super_user";
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ApplicationStartup(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        initialDataLoad();
    }

    private void initialDataLoad() {
        loadRole();
        loadUser();
    }

    private void loadRole() {
        try{
            RoleDto superUserRoleDto = new RoleDto.RoleDtoBuilder(SUPER_USER_ROLE_NAME)
                    .withPermission(CREATE_USER)
                    .withPermission(DELETE_USER)
                    .withPermission(UPDATE_USER)
                    .withPermission(LIST_USER)
                    .build();
            roleService.addRole(superUserRoleDto);
        } catch (IllegalStateException e) {
            LOGGER.info("Role {} already exists", SUPER_USER_ROLE_NAME);
        }
    }

    private void loadUser() {
        try{
            UserCredentials userCredentials = new UserCredentials(ADMIN_USER_NAME, AuthUtil.hash(AuthUtil.generatePassword()));
            UserDto adminUser = new UserDto.UserDtoBuilder(userCredentials)
                    .withRoleName(SUPER_USER_ROLE_NAME)
                    .build();
            userService.addUser(adminUser);
        } catch (IllegalStateException e) {
            LOGGER.info("User {} already exists", ADMIN_USER_NAME);
        }
    }

}