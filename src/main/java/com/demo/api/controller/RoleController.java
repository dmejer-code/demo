package com.demo.api.controller;

import com.demo.api.dto.RoleDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public List<RoleEntity> listRoles(@RequestParam(required = false, defaultValue = "0") int page) {
        return roleService.getAllRoles(page);
    }

    @PostMapping
    public RoleEntity createRole(@Valid @NotNull @RequestBody RoleDto roleDto) {
        try {
            return roleService.addRole(roleDto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public void deleteRole(@PathVariable("id") @NotNull UUID id) {
        try {
            roleService.removeRole(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The role is in use");
        }
    }

    @PutMapping(path = "{id}")
    public RoleEntity updateRole(@PathVariable("id") UUID id, @Valid @NotNull @RequestBody RoleDto roleDto) {
        try {
            return roleService.updateRoleById(id, roleDto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

}
