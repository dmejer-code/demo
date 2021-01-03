package com.demo.api.util;

import com.demo.api.dto.UserDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserEntity;

import java.util.Set;

public final class UserDtoConverter {

    private UserDtoConverter() {
        // no action required
    }

    public static UserDto mapToDto(UserEntity userEntity) {
        return new UserDto.UserDtoBuilder(userEntity.getUserCredentials())
                .withRoleNames(userEntity.getRoleNames())
                .build();
    }

    public static UserEntity mapToEntity(UserDto userDto, Set<RoleEntity> roles) {
        return new UserEntity.UserEntityBuilder(userDto.getUserCredentials())
                .withRoles(roles)
                .build();
    }

}
