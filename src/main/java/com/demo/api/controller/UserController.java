package com.demo.api.controller;

import com.demo.api.dto.UserDto;
import com.demo.api.model.UserEntity;
import com.demo.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserEntity> listUsers(@RequestParam(required = false, defaultValue = "0") int page) {
        return userService.getAllUsers(page);
    }

    @PostMapping
    public UserEntity createUser(@Valid @NotNull @RequestBody UserDto userDto) {
        try {
            return userService.addUser(userDto);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

    @DeleteMapping(path = "{id}")
    public void deleteUser(@PathVariable("id") @NotNull UUID id) {
        userService.removeUser(id);
    }

    @PutMapping(path = "{id}")
    public UserEntity updateUser(@PathVariable("id") @NotNull UUID id, @Valid @NotNull @RequestBody UserDto user) {
        try {
            return userService.updatePersonById(id, user);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
    }

}
