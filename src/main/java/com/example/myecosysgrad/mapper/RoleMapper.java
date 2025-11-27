package com.example.myecosysgrad.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.RoleRequest;
import com.example.myecosysgrad.dto.admin.response.RoleResponse;
import com.example.myecosysgrad.dto.admin.response.SimplePermissionResponse;
import com.example.myecosysgrad.dto.admin.response.SimpleUserResponse;
import com.example.myecosysgrad.model.Permission;
import com.example.myecosysgrad.model.Role;
import com.example.myecosysgrad.model.RolePermission;
import com.example.myecosysgrad.model.UserRole;

import lombok.SneakyThrows;

@Mapper(
        componentModel = "spring",
        uses = {PermissionMapper.class}) // uses = {PermissionMapper.class} để tránh circular dependency
public interface RoleMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    Role toRole(RoleRequest roleRequest);

    //    @Mapping(target = "permissions", expression =
    // "java(mapPermissions(role.getRolePermissions()))")
    //    RoleResponse toRoleResponse(Role role);
    //
    //    // hàm helper để convert từ Set<RolePermission> -> Set<PermissionResponse>
    //    default Set<PermissionResponse> mapPermissions(Set<RolePermission> rolePermissions) {
    //        if (rolePermissions == null) return java.util.Collections.emptySet();
    //        return rolePermissions.stream()
    //                .map(RolePermission::getPermission)
    //                .map(permission -> new PermissionResponse(permission.getName(),
    // permission.getDescription()))
    //                .collect(java.util.stream.Collectors.toSet());
    //    }

    @Mapping(target = "permissions", source = "rolePermissions")
    @Mapping(target = "users", source = "userRoles")
    RoleResponse toRoleResponse(Role role);

    //    default Set<PermissionResponse> toPermissionResponseSet(Set<RolePermission>
    // rolePermissions) {
    //        return rolePermissions.stream()
    //                .map(rolePermission -> PermissionResponse.builder()
    //                        .name(rolePermission.getPermission().getName())
    //                        .description(rolePermission.getPermission().getDescription())
    //                        .build())
    //                .collect(Collectors.toSet());
    //    }

    @Named("RolePermissionToSimplePermissionResponse")
    @SneakyThrows // Lombok tự bắt NullPointerException nếu rolePermission.getPermission() là null
    default SimplePermissionResponse rolePermissionToSimplePermissionResponse(RolePermission rolePermission) {
        //        if (rolePermission == null || rolePermission.getPermission() == null) {
        //            return null; // hoặc throw, hoặc log
        //        }
        return SimplePermissionResponse.builder()
                .name(rolePermission.getPermission().getName())
                .description(rolePermission.getPermission().getDescription())
                .build();
    }

    @IterableMapping(qualifiedByName = "RolePermissionToSimplePermissionResponse")
    Set<SimplePermissionResponse> rolePermissionsToSimplePermissionResponse(Set<RolePermission> rolePermissions);

    // => MapStruct tự động sinh code:
    /* javaSet<PermissionResponse> result = new HashSet<>();
    for (RolePermission rp : rolePermissions) {
    	result.add(rolePermissionToPermissionResponse(rp));
    }
    return result; */

    @Named("UserRoleToSimpleUserResponse")
    @SneakyThrows
    default SimpleUserResponse userRoleToSimpleUserResponse(UserRole userRole) {
        return SimpleUserResponse.builder()
                .username(userRole.getUser().getUsername())
                .build();
    }

    @IterableMapping(qualifiedByName = "UserRoleToSimpleUserResponse")
    Set<SimpleUserResponse> userRolesToSimpleUserResponse(Set<UserRole> userRoles);

    @Mapping(target = "rolePermissions", ignore = true)
    @Mapping(target = "userRoles", ignore = true)
    void updateRole(@MappingTarget Role role, RoleRequest roleRequest);

    default Role toRoleWithPermissions(RoleRequest request, Set<Permission> permissions) {
        Role role = toRole(request);
        Set<RolePermission> rolePermissions = permissions.stream()
                .map(p -> {
                    RolePermission rp = new RolePermission();
                    rp.setRole(role);
                    rp.setPermission(p);
                    return rp;
                })
                .collect(Collectors.toSet());
        role.setRolePermissions(rolePermissions);
        rolePermissions.forEach(rp -> rp.setRole(role));
        return role;
    }
}
