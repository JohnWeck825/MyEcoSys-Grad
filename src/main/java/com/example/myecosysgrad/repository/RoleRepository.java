package com.example.myecosysgrad.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.myecosysgrad.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByName(String name);

    Set<Role> findAllByNameIn(Set<String> names);

    @Query("SELECT r FROM Role r JOIN r.rolePermissions rp WHERE rp.permission.id = :permissionId")
    Set<Role> findRolesByPermissionId(@Param("permissionId") Integer permissionId);

    boolean existsRoleByName(String name);
}
