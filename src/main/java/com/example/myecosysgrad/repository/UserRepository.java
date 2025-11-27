package com.example.myecosysgrad.repository;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.myecosysgrad.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Set<User> findAllByUsernameIn(Set<String> names);

    //    @EntityGraph(attributePaths = {
    //            "userRoles",
    //            "userRoles.role",
    //            "userRoles.role.rolePermissions",
    //            "userRoles.role.rolePermissions.permission"
    //    })
    //    Optional<User> findByUserId(String userid);

    @Query(
            """
				SELECT u FROM User u
				JOIN FETCH u.userRoles ur
				JOIN FETCH ur.role r
				LEFT JOIN FETCH r.rolePermissions rp
				LEFT JOIN FETCH rp.permission p
				WHERE u.id = :id
			""")
    Optional<User> findUserWithRolesAndPermissions(Long id);

    @Query(
            """
				SELECT u FROM User u
				JOIN FETCH u.userRoles ur
				JOIN FETCH ur.role r
				LEFT JOIN FETCH r.rolePermissions rp
				LEFT JOIN FETCH rp.permission p
				WHERE u.username = :username
			""")
    Optional<User> findByUsernameWithAllInfo(String username);
}
