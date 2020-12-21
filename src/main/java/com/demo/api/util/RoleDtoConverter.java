package com.demo.api.util;

import com.demo.api.dto.RoleDto;
import com.demo.api.model.RoleEntity;

public final class RoleDtoConverter {

    private RoleDtoConverter() {
        // no action required
    }

    public static RoleDto mapToDto(RoleEntity roleEntity) {
        return new RoleDto.RoleDtoBuilder(roleEntity.getName())
                .createUser(roleEntity.isCreateUser())
                .updateUser(roleEntity.isUpdateUser())
                .deleteUser(roleEntity.isDeleteUser())
                .listUser(roleEntity.isListUser())
                .build();
    }

    public static RoleEntity mapToEntity(RoleDto roleDto) {
        return new RoleEntity.RoleEntityBuilder(roleDto.getName())
                .createUser(roleDto.isCreateUser())
                .updateUser(roleDto.isUpdateUser())
                .deleteUser(roleDto.isDeleteUser())
                .listUser(roleDto.isListUser())
                .build();
    }

}
