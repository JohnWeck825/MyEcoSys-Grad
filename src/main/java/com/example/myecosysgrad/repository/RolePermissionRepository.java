package com.example.myecosysgrad.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.myecosysgrad.model.Role;
import com.example.myecosysgrad.model.RolePermission;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Integer> {
    Set<RolePermission> findByRole(Role role);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.role.id = :roleId")
    void deleteRolePermissionsByRoleId(@Param("roleId") Integer roleId);

    void deleteRolePermissionsByPermission_Id(Integer permissionId);

    @Modifying
    @Query("DELETE FROM RolePermission rp WHERE rp.permission.id = :permissionId")
    void deleteByPermissionId(@Param("permissionId") Integer permissionId);
}
