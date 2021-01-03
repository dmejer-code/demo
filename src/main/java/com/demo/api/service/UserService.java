package com.demo.api.service;

import com.demo.api.dao.UserDao;
import com.demo.api.dto.PasswordChangeDto;
import com.demo.api.dto.UserDto;
import com.demo.api.model.RoleEntity;
import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import com.demo.api.util.AuthUtil;
import com.demo.api.util.UserDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    @Transactional(readOnly = true)
    public UserEntity findById(UUID id) {
        return userDao.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public UserEntity findByUserName(String userName) {
        return userDao.findByUserCredentialsName(userName).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<UserEntity> getAllUsers(int page) {
        int pageNumber = Math.max(page, 0);
        return userDao.findAll(PageRequest.of(pageNumber, PAGE_SIZE)).toList();
    }

    @Transactional
    public UserEntity addUser(UserDto userDto) {
        if (findByUserName(userDto.getName()) != null) {
            throw new IllegalStateException("User already exists");
        }

        Set<RoleEntity> roles = roleService.verifyRole(userDto.getRoleNames());
        UserEntity userEntity = UserDtoConverter.mapToEntity(userDto, roles);
        userEntity.getUserCredentials().hash();

        return userDao.save(userEntity);
    }

    public void removeUser(UUID id) {
        userDao.deleteById(id);
    }

    @Transactional
    public UserEntity updatePersonById(UUID id, UserDto userDto) {
        if (userDao.findById(id).isEmpty()) {
            throw new IllegalStateException("User does not exist");
        }

        Set<RoleEntity> roleEntity = roleService.verifyRole(userDto.getRoleNames());
        UserEntity userEntity = UserDtoConverter.mapToEntity(userDto, roleEntity);
        userEntity.getUserCredentials().hash();
        userEntity.setId(id);

        return userDao.save(userEntity);
    }

    @Transactional(readOnly = true)
    public Optional<UserEntity> getAuthenticatedUser(UserCredentials credentials) {
        credentials.hash();
        return userDao.findByUserCredentials(credentials);
    }

    public boolean changePassword(UUID userId, String newPassword) {
        return userDao.changeUserPassword(userId, AuthUtil.hash(newPassword)) > 0;
    }

    @Transactional
    public boolean changePassword(UUID userId, PasswordChangeDto passwordDto) {
        return !passwordDto.isOfSameValue()
                && userDao.changeUserPassword(userId, AuthUtil.hash(passwordDto.getOldPassword()), AuthUtil.hash(passwordDto.getNewPassword())) > 0;
    }

}
