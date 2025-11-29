package com.example.myecosysgrad.controller.admin;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import com.example.myecosysgrad.dto.admin.request.PermissionRequest;
import com.example.myecosysgrad.dto.admin.response.PermissionOptionResponse;
import com.example.myecosysgrad.dto.admin.response.PermissionResponse;
import com.example.myecosysgrad.dto.api.request.ApiResponse;
import com.example.myecosysgrad.service.admin.PermissionService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/permissions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionController {
    PermissionService permissionService;

    @GetMapping()
    ApiResponse<Page<PermissionResponse>> getPermissions(
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "name") String softField,
            @RequestParam(defaultValue = "asc") String softDir
    ) {
        return ApiResponse.success(permissionService.getPermissions(keyword, page, size, softField, softDir));
    }

    @PostMapping("/create")
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.createPermission(permissionRequest))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<PermissionResponse> updatePermission(
            @PathVariable Integer id, @RequestBody PermissionRequest permissionRequest) {
        return ApiResponse.<PermissionResponse>builder()
                .result(permissionService.updatePermission2(id, permissionRequest))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    ApiResponse<Void> deletePermission(@PathVariable Integer id) {
        permissionService.deletePermission(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping("/options")
    ApiResponse<List<PermissionOptionResponse>> getPermissionOptions() {
        return ApiResponse.success(permissionService.getPermissionOptions());
    }
}
