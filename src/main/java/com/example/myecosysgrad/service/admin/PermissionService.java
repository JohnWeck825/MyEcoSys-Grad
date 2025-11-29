package com.example.myecosysgrad.service.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManagerFactory;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myecosysgrad.dto.admin.request.PermissionRequest;
import com.example.myecosysgrad.dto.admin.response.PermissionOptionResponse;
import com.example.myecosysgrad.dto.admin.response.PermissionResponse;
import com.example.myecosysgrad.exception.AppException;
import com.example.myecosysgrad.exception.ErrorCode;
import com.example.myecosysgrad.mapper.PermissionMapper;
import com.example.myecosysgrad.model.Permission;
import com.example.myecosysgrad.model.Role;
import com.example.myecosysgrad.model.RolePermission;
import com.example.myecosysgrad.repository.PermissionRepository;
import com.example.myecosysgrad.repository.RolePermissionRepository;
import com.example.myecosysgrad.repository.RoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class PermissionService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    PermissionMapper permissionMapper;
    RolePermissionRepository rolePermissionRepository;
    EntityManagerFactory entityManagerFactory;

    public PermissionResponse createPermission(PermissionRequest permissionRequest) {
        Permission permission = permissionMapper.toPermission(permissionRequest);
        permission = permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public Page<PermissionResponse> getPermissions(String keyword,
                                                   Integer page,
                                                   Integer size,
                                                   String softField,
                                                   String softDir) {

        Sort sort = softDir.equalsIgnoreCase("asc")
                ? Sort.by(softField).ascending()
                : Sort.by(softField).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Permission> permissions;

        if (keyword != null && !keyword.trim().isEmpty()) {
            permissions = permissionRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            permissions = permissionRepository.findAll(pageable);
        }

        return permissions.map(permissionMapper::toPermissionResponse);
    }

    @Transactional
    public PermissionResponse updatePermission(Integer id, PermissionRequest permissionRequest) {
        Permission existingPermission =
                permissionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        permissionMapper.updatePermission(existingPermission, permissionRequest);

        //        rolePermissionRepository.deleteByPermissionId(id);
        // Lấy ra các role có permission_id = id
        //        Set<Role> roles = roleRepository.findRolesByPermissionId(id);
        //        Set<Role> roles = roleRepository.findAllByNameIn(permissionRequest.getRoles());

        //        //Lấy danh sách các role hiện đang có permission này
        //        Set<Role> currentRoles = existingPermission.getRolePermissions().stream()
        //                .map(RolePermission::getRole)
        //                .collect(Collectors.toSet());

        // Refresh cache các Role bị ảnh hưởng
        // 1. Lấy danh sách Role hiện có permission này
        Set<Role> affectedRoles = existingPermission.getRolePermissions().stream()
                .map(RolePermission::getRole)
                .collect(Collectors.toSet());
        // 2. Xóa các RolePermission có permission_id = id
        rolePermissionRepository.deleteByPermissionId(id);

        Set<Role> newRoles = roleRepository.findAllByNameIn(permissionRequest.getRoles());
        newRoles.forEach(role -> {
            RolePermission rolePermission = RolePermission.builder()
                    .role(role)
                    .permission(existingPermission)
                    .build();
            role.getRolePermissions().add(rolePermission);
            existingPermission.getRolePermissions().add(rolePermission);
        });

        affectedRoles.forEach(role -> entityManagerFactory.createEntityManager().refresh(role));

        //        roles.forEach(role -> {
        //            RolePermission rolePermission = RolePermission.builder()
        //                    .role(role)
        //                    .permission(existingPermission)
        //                    .build();
        //            existingPermission.getRolePermissions().add(rolePermission);
        //
        //        });

        return permissionMapper.toPermissionResponse(existingPermission);
    }

    public List<PermissionOptionResponse> getPermissionOptions() {
        return permissionRepository.findAll().stream()
                .map(permission -> PermissionOptionResponse.builder()
                        .id(permission.getId())
                        .name(permission.getName())
                        .description(permission.getDescription())
                        .build())
                .toList();
    }

    @Transactional
    public PermissionResponse updatePermission2(Integer id, PermissionRequest request) {
        Permission permission =
                permissionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));

        // 1. Cập nhật field cơ bản
        permissionMapper.updatePermission(permission, request);

        // 2. Lấy danh sách Role mới từ request
        Set<Role> desiredRoles = roleRepository.findAllByNameIn(request.getRoles());

        // 3. Lấy danh sách Role hiện tại đang có Permission này
        Set<Role> currentRoles = permission.getRolePermissions().stream()
                .map(RolePermission::getRole)
                .collect(Collectors.toSet());

        // Xóa những Role không còn trong danh sách mới
        currentRoles.stream()
                .filter(role -> !desiredRoles.contains(role)) // lọc ra các role không có trong danh sách mới
                // (request)
                .forEach(
                        roleToRemove -> { // thao tác với từng phần tử vừa lọc
                            RolePermission rpToRemoveRecord = permission.getRolePermissions().stream()
                                    .filter(rp -> rp.getRole().equals(roleToRemove))
                                    .findFirst()
                                    .orElseThrow();

                            // Đồng bộ 2 chiều
                            roleToRemove.getRolePermissions().remove(rpToRemoveRecord);
                            permission.getRolePermissions().remove(rpToRemoveRecord);

                            //                    rolePermissionRepository.delete(rpToRemoveRecord);
                        });

        // Thêm những Role mới
        desiredRoles.stream()
                .filter(role -> !currentRoles.contains(role)) // lọc ra các role không có trong danh sách hiện tại
                // - các role mới
                .forEach(
                        roleToAdd -> { // thao tác với từng phần tử vừa lọc
                            RolePermission newRPLink = RolePermission.builder()
                                    .role(roleToAdd)
                                    .permission(permission)
                                    .build();

                            // Đồng bộ 2 chiều – cực kỳ quan trọng!
                            roleToAdd.getRolePermissions().add(newRPLink);
                            permission.getRolePermissions().add(newRPLink);

                            //                    rolePermissionRepository.save(newRPLink);
                        });

        // Không cần save(permission) – đã thay đổi collection trong managed entity
        return permissionMapper.toPermissionResponse(permission);
    }

    @Transactional
    public void deletePermission(Integer id) {
        rolePermissionRepository.deleteByPermissionId(id);
        permissionRepository.deleteById(id);
    }
}
