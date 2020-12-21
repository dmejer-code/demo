package com.demo.api.service;

import com.demo.api.dao.UserDao;
import com.demo.api.dto.PasswordChangeDto;
import com.demo.api.dto.UserDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.util.UserDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final int PAGE_SIZE = 10;

    private final UserDao userDao;
    private final RoleService roleService;

    @Autowired
    public UserService(UserDao userDao, RoleService roleService) {
        this.userDao = userDao;
        this.roleService = roleService;
    }

    public UserEntity findById(UUID id) {
        return userDao.findById(id).orElse(null);
    }

    public Optional<UserEntity> findByUserName(String userName) {
        return userDao.findByUserCredentialsName(userName);
    }

    public List<UserEntity> getAllUsers(int page) {
        int pageNumber = Math.max(page, 0);
        return userDao.findAll(PageRequest.of(pageNumber, PAGE_SIZE))
                .toList();
    }

    @Transactional
    public Optional<UserEntity> addUser(UserDto userDto) {
        Optional<UserEntity> userEntity = findByUserName(userDto.getName());

        if (userEntity.isPresent()) {
            return Optional.empty();
        }

        RoleEntity roleEntity = roleService.getRoleByName(userDto.getRoleName());
        UserEntity newUserEntity = UserDtoConverter.mapToEntity(userDto, roleEntity);
        return Optional.of(userDao.save(newUserEntity));
    }

    public void removeUser(UUID id) {
        userDao.deleteById(id);
    }

    @Transactional
    public Optional<UserEntity> updatePersonById(UUID id, UserDto userDto) {
        Optional<UserEntity> userEntity = userDao.findById(id);

        if (userEntity.isPresent()) {
            return Optional.empty();
        }

        RoleEntity roleEntity = roleService.getRoleByName(userDto.getRoleName());
        UserEntity newUserEntity = UserDtoConverter.mapToEntity(userDto, roleEntity);
        newUserEntity.setId(id);
        return Optional.of(userDao.save(newUserEntity));
    }

    public Optional<UserEntity> getAuthenticatedUser(UserCredentials credentials) {
        return userDao.findByUserCredentials(credentials);
    }

    public boolean changePassword(UUID userId, String newPassword) {
        return userDao.changeUserPassword(userId, newPassword) > 0;
    }

    public boolean changePassword(UUID userId, PasswordChangeDto password) {
        return userDao.changeUserPassword(userId, password.getOldPassword(), password.getNewPassword()) > 0;
    }

}
