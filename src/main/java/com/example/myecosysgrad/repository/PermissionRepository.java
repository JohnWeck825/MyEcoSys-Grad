package com.example.myecosysgrad.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.myecosysgrad.model.Permission;

public interface PermissionRepository extends JpaRepository<Permission, Integer> {
    Set<Permission> findAllByNameIn(Set<String> names);

    Permission findByName(String name);

    @Query("SELECT p FROM Permission p "
            + "LEFT JOIN FETCH p.rolePermissions rp "
            + "LEFT JOIN FETCH rp.role "
            + "WHERE p.id = :id")
    //    @EntityGraph(attributePaths = {"rolePermissions", "rolePermissions.role"})
    @Override
    Optional<Permission> findById(Integer id);

    //    @Query("SELECT p FROM Permission p " +
    //            "LEFT JOIN FETCH p.rolePermissions rp " +
    //            "LEFT JOIN FETCH rp.role " +
    //            "WHERE p.id = :id")
    //    @EntityGraph(attributePaths = {"rolePermissions", "rolePermissions.role"})
    //    Optional<Permission> findById(@Param("id") Integer id); //Find Permission with Roles

    Page<Permission> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
