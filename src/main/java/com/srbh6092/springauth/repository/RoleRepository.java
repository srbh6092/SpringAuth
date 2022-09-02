package com.srbh6092.springauth.repository;

import com.srbh6092.springauth.entity.Role;
import com.srbh6092.springauth.entity.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findRoleByName(RoleType user);
}
