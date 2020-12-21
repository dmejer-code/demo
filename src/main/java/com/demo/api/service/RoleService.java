package com.demo.api.service;

import com.demo.api.dao.RoleDao;
import com.demo.api.dto.RoleDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.util.RoleDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RoleService {
    private static final int PAGE_SIZE = 10;

    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    public RoleEntity getRoleByName(String roleName) {
        return roleDao.findByName(roleName).orElse(null);
    }

    public List<RoleEntity> getAllRoles(int page) {
        int pageNumber =  Math.max(page, 0);
        return roleDao.findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .toList();
    }

    @Transactional
    public Optional<RoleEntity> addRole(RoleDto roleDto) {
        Optional<RoleEntity> roleEntity = roleDao.findByName(roleDto.getName());

        if (roleEntity.isPresent()) {
            return Optional.empty();
        }

        RoleEntity newRoleEntity = RoleDtoConverter.mapToEntity(roleDto);
        return Optional.of(roleDao.save(newRoleEntity));
    }

    public void removeRole(UUID id) {
        roleDao.deleteById(id);
    }

    @Transactional
    public Optional<RoleEntity> updateRoleById(UUID id, RoleDto roleDto) {
        Optional<RoleEntity> roleEntity = roleDao.findById(id);

        if (roleEntity.isPresent()) {
            return Optional.empty();
        }

        RoleEntity newRoleEntity = RoleDtoConverter.mapToEntity(roleDto);
        newRoleEntity.setId(id);
        return  Optional.of(roleDao.save(newRoleEntity));
    }

}
