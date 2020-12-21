package com.demo.api.config;

import static com.demo.api.model.UserCredentials.*;

import com.demo.api.dto.RoleDto;
import com.demo.api.dto.UserDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserCredentials;
import com.demo.api.service.RoleService;
import com.demo.api.service.UserService;
import com.demo.api.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
    private static final String SUPER_USER_ROLE_NAME = "super_user";
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public ApplicationStartup(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        initialDataLoad();
    }

    private void initialDataLoad() {
        RoleDto superUserRoleDto = new RoleDto.RoleDtoBuilder(SUPER_USER_ROLE_NAME)
                .createUser(true)
                .deleteUser(true)
                .updateUser(true)
                .listUser(true)
                .build();

        roleService.addRole(superUserRoleDto);
        UserCredentials userCredentials = new UserCredentials(ADMIN_USER_NAME, AuthUtil.generatePassword());
        UserDto adminUser = new UserDto.UserDtoBuilder(userCredentials)
                .roleName(SUPER_USER_ROLE_NAME)
                .build();
        userService.addUser(adminUser);
    }

}