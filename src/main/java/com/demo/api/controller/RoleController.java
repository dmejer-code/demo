package com.demo.api.controller;

import com.demo.api.dto.RoleDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
public class RoleController {

    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public List<RoleEntity> listRoles(@RequestParam(required = false, defaultValue = "0") int page) {
        return roleService.getAllRoles(page);
    }

    @PostMapping("/roles")
    public RoleEntity createRole(@Valid @NotNull @RequestBody RoleDto roleDto) {
        return roleService.addRole(roleDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Role already exists"));
    }

    @DeleteMapping(path = "roles/{id}")
    public void deleteRole(@PathVariable("id") @NotNull UUID id) {
        roleService.removeRole(id);
    }

    @PutMapping(path = "roles/{id}")
    public RoleEntity updateRole(@PathVariable("id") UUID id, @Valid @NotNull @RequestBody RoleDto roleDto) {
        return roleService.updateRoleById(id, roleDto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Role already exists"));
    }

}
