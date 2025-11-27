package com.example.myecosysgrad.mapper;

import java.util.Set;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.UserCreationRequest;
import com.example.myecosysgrad.dto.admin.request.UserUpdateRequest;
import com.example.myecosysgrad.dto.admin.response.SimpleRoleResponse;
import com.example.myecosysgrad.dto.admin.response.UserResponse;
import com.example.myecosysgrad.model.User;
import com.example.myecosysgrad.model.UserRole;

import lombok.SneakyThrows;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userRoles", ignore = true)
    User toUser(UserCreationRequest request);

    //    @Mapping(target = "id", ignore = true)
    //    @Mapping(source = "id", target = "id")
    @Mapping(target = "roles", source = "userRoles")
    UserResponse toUserResponse(User user);

    @Named("UserRoleToSimpleRoleResponse")
    @SneakyThrows
    default SimpleRoleResponse userRoleToSimpleRoleResponse(UserRole userRole) {
        if (userRole == null || userRole.getRole() == null) {
            return null;
        }
        return SimpleRoleResponse.builder()
                .name(userRole.getRole().getName())
                .description(userRole.getRole().getDescription())
                .build();
    }

    @IterableMapping(qualifiedByName = "UserRoleToSimpleRoleResponse")
    Set<SimpleRoleResponse> userRolesToSimpleRoleResponse(Set<UserRole> userRoles);

    @Mapping(target = "userRoles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
