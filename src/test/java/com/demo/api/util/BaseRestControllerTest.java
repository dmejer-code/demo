package com.demo.api.util;

import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseRestControllerTest {

    @Autowired
    private UserService userService;

    protected UserEntity getAdminUser() {
        return userService.findByUserName(UserCredentials.ADMIN_USER_NAME).orElseThrow();
    }

}
