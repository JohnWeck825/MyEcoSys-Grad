package com.example.myecosysgrad.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myecosysgrad.dto.admin.request.RoleRequest;
import com.example.myecosysgrad.dto.admin.response.RoleOptionResponse;
import com.example.myecosysgrad.dto.admin.response.RoleResponse;
import com.example.myecosysgrad.dto.api.request.ApiResponse;
import com.example.myecosysgrad.service.admin.RoleService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleController {
    RoleService roleService;

    @GetMapping()
    ApiResponse<Page<RoleResponse>> getRoles(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "name") String softField,
            @RequestParam(defaultValue = "asc") String softDir
    ) {
        return ApiResponse.success(roleService.getRoles(keyword, page, size, softField, softDir));
    }

    @PostMapping("/create")
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.createRole(roleRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<Void> deleteRole(@PathVariable Integer id) {
        roleService.deleteRole(id);
        return ApiResponse.success(null);
    }

    @PutMapping("/{id}")
    ApiResponse<RoleResponse> updateRole(@PathVariable Integer id, @RequestBody RoleRequest roleRequest) {
        return ApiResponse.<RoleResponse>builder()
                .result(roleService.updateRole(id, roleRequest))
                .build();
    }

    // Lấy danh sách Role cho create user và update user
    @GetMapping("/options")
    ApiResponse<List<RoleOptionResponse>> getRoleOptions() {
        return ApiResponse.success(roleService.getRoleOptions());
    }
}
