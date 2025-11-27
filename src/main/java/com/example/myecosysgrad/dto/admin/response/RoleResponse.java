package com.example.myecosysgrad.dto.admin.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    Integer id;
    String name;
    String description;
    Set<SimplePermissionResponse> permissions;
    Set<SimpleUserResponse> users;
}
