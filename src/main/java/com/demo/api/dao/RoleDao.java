package com.demo.api.dao;

import com.demo.api.model.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleDao extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByName(String roleName);

    Set<RoleEntity> findByNameIn(Set<String> roleName);

}
