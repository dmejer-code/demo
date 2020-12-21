package com.demo.api.util;

import com.demo.api.dto.UserDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserEntity;

public final class UserDtoConverter {

    private UserDtoConverter() {
        // no action required
    }

    public static UserDto mapToDto(UserEntity userEntity) {
        return new UserDto.UserDtoBuilder(userEntity.getUserCredentials())
                .roleName(userEntity.getRoleName())
                .build();
    }

    public static UserEntity mapToEntity(UserDto userDto, RoleEntity roleEntity) {
        return new UserEntity.UserEntityBuilder(userDto.getUserCredentials())
                .roleEntity(roleEntity)
                .build();
    }

}
