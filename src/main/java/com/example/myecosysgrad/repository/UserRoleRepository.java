package com.example.myecosysgrad.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.myecosysgrad.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Integer> {

    @Modifying
    @Query("DELETE FROM UserRole ur WHERE ur.user.id = :userId")
    void deleteUserRolesByUserId(Integer userId);
}
