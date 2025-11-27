package com.example.myecosysgrad.mapper;

import java.util.Set;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.PermissionRequest;
import com.example.myecosysgrad.dto.admin.response.PermissionResponse;
import com.example.myecosysgrad.dto.admin.response.SimpleRoleResponse;
import com.example.myecosysgrad.model.Permission;
import com.example.myecosysgrad.model.RolePermission;

@Mapper(
        componentModel = "spring",
        uses = {RoleMapper.class})
public interface PermissionMapper {

    @Mapping(target = "rolePermissions", ignore = true)
    Permission toPermission(PermissionRequest permissionRequest);

    @Mapping(target = "roles", source = "rolePermissions")
    PermissionResponse toPermissionResponse(Permission permission);

    @Named("RolePermissionToSimpleRoleResponse")
    default SimpleRoleResponse rolePermissionToSimpleRoleResponse(RolePermission rolePermission) {
        if (rolePermission == null || rolePermission.getRole() == null) {
            return null;
        }

        //        Role role = rolePermission.getRole();

        return SimpleRoleResponse.builder()
                .name(rolePermission.getRole().getName())
                .description(rolePermission.getRole().getDescription())
                .build();
    }

    @IterableMapping(qualifiedByName = "RolePermissionToSimpleRoleResponse")
    Set<SimpleRoleResponse> rolePermissionsToRoleResponses(Set<RolePermission> rolePermissions);

    @Mapping(target = "rolePermissions", ignore = true)
    void updatePermission(@MappingTarget Permission existingPermission, PermissionRequest permissionRequest);
}
