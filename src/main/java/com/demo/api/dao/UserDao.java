package com.demo.api.dao;

import com.demo.api.model.UserCredentials;
import com.demo.api.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserDao extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUserCredentialsName(String name);
    Optional<UserEntity> findByUserCredentials(UserCredentials userCredentials);

    @Modifying
    @Query("update user u set u.userCredentials.password = ?2 where u.id = ?1")
    int changeUserPassword(UUID id, String newPassword);

    @Modifying
    @Query("update user u set u.userCredentials.password = ?3 where u.id = ?1 and u.userCredentials.password = ?2")
    int changeUserPassword(UUID id, String oldPassword, String newPassword);
}