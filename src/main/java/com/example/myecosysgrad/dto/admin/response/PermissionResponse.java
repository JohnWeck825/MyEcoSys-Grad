package com.example.myecosysgrad.dto.admin.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionResponse {
    Integer id;
    String name;
    String description;
    Set<SimpleRoleResponse> roles;
}
