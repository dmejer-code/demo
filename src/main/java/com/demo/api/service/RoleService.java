package com.demo.api.service;

import com.demo.api.dao.RoleDao;
import com.demo.api.dto.RoleDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.util.RoleDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class RoleService {
    private static final int PAGE_SIZE = 10;

    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Transactional(readOnly = true)
    public Set<RoleEntity> getRoleByName(Set<String> roleNames) {
        if (CollectionUtils.isEmpty(roleNames)) {
            return Collections.emptySet();
        }

        return roleDao.findByNameIn(roleNames);
    }

    @Transactional(readOnly = true)
    public List<RoleEntity> getAllRoles(int page) {
        int pageNumber = Math.max(page, 0);
        return roleDao.findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .toList();
    }

    @Transactional
    public RoleEntity addRole(RoleDto roleDto) {
        if (roleDao.findByName(roleDto.getName()).isPresent()) {
            throw new IllegalStateException("Role already exists");
        }

        RoleEntity newRoleEntity = RoleDtoConverter.mapToEntity(roleDto);
        return roleDao.save(newRoleEntity);
    }

    public void removeRole(UUID id) {
        roleDao.deleteById(id);
    }

    @Transactional
    public RoleEntity updateRoleById(UUID id, RoleDto roleDto) {
        if (roleDao.findById(id).isEmpty()) {
            throw new IllegalStateException("Role does not exist");
        }

        RoleEntity newRoleEntity = RoleDtoConverter.mapToEntity(roleDto);
        newRoleEntity.setId(id);

        return roleDao.save(newRoleEntity);
    }

    public Set<RoleEntity> verifyRole(Set<String> roleNames) {
        Set<RoleEntity> roles = getRoleByName(roleNames);

        if (roleNames.size() != roles.size()) {
            throw new IllegalStateException("Role does not exist");
        }

        return roles;
    }

}
