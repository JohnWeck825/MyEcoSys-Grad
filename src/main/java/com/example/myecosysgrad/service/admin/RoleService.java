package com.example.myecosysgrad.service.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myecosysgrad.dto.admin.request.RoleRequest;
import com.example.myecosysgrad.dto.admin.response.RoleOptionResponse;
import com.example.myecosysgrad.dto.admin.response.RoleResponse;
import com.example.myecosysgrad.exception.AppException;
import com.example.myecosysgrad.exception.ErrorCode;
import com.example.myecosysgrad.mapper.RoleMapper;
import com.example.myecosysgrad.model.*;
import com.example.myecosysgrad.repository.*;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RoleService {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    RoleMapper roleMapper;
    RolePermissionRepository rolePermissionRepository;
    UserRoleRepository userRoleRepository;
    UserRepository userRepository;

    @Transactional
    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepository.existsRoleByName(roleRequest.getName())) {
            throw new AppException(ErrorCode.ROLE_EXISTED);
        }

        Role role = roleMapper.toRole(roleRequest);
        Role savedRole = roleRepository.save(role);
        //
        Set<Permission> permissions = permissionRepository.findAllByNameIn(roleRequest.getPermissions());
        ////        Role role = roleMapper.toRoleWithPermissions(roleRequest, permissions);
        //        Set<RolePermission> rolePermissions = permissions.stream()
        //                        .map(permission -> {
        //                            RolePermission rolePermission = new RolePermission();
        //                            rolePermission.setRole(savedRole);
        //                            rolePermission.setPermission(permission);
        //                            return  rolePermission;
        //                        })
        //                .collect(Collectors.toSet());
        //
        //        rolePermissionRepository.saveAll(rolePermissions);
        //
        //        savedRole.setRolePermissions(rolePermissions);
        //        roleRepository.save(savedRole);
        //        roleRepository.save(role);

        //        if(!permissions.isEmpty()) {
        //            permissions.forEach(p -> {
        //                Permission permission = permissionRepository.findByName(p.getName());
        //                rolePermissionRepository.save(new RolePermission(savedRole, permission));
        //            });
        ////
        // savedRole.setRolePermissions(rolePermissionRepository.findByRole(savedRole));
        ////            roleRepository.save(savedRole);
        //        }

        Set<RolePermission> rolePermissions =
                permissions.stream().map(p -> new RolePermission(savedRole, p)).collect(Collectors.toSet());
        rolePermissionRepository.saveAll(rolePermissions);
        savedRole.setRolePermissions(rolePermissions);
        Role reloaded = roleRepository.findById(savedRole.getId()).get();
        return roleMapper.toRoleResponse(reloaded);
    }

    @Transactional
    public RoleResponse createRoleTest(RoleRequest roleRequest) {
        Role role = roleMapper.toRole(roleRequest);
        Role savedRole = roleRepository.save(role);

        Set<RolePermission> rolePermissions =
                permissionRepository.findAllByNameIn(roleRequest.getPermissions()).stream()
                        .map(p -> new RolePermission(savedRole, p))
                        .collect(Collectors.toSet());

        savedRole.setRolePermissions(rolePermissions);

        Role reloaded = roleRepository.findById(savedRole.getId()).get();
        return roleMapper.toRoleResponse(reloaded);
    }

    public Page<RoleResponse> getRoles(String keyword,
                                       Integer page,
                                       Integer size,
                                       String softField,
                                       String softDir) {

        Sort sort = softDir.equalsIgnoreCase("asc")
                ? Sort.by(softField).ascending()
                : Sort.by(softField).descending();

        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Role> roles;

        if (keyword != null && !keyword.trim().isEmpty()) {
            roles = roleRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else {
            roles = roleRepository.findAll(pageable);
        }

        return roles.map(roleMapper::toRoleResponse);
    }

    @Transactional
    public void deleteRole(Integer id) {
        rolePermissionRepository.deleteRolePermissionsByRoleId(id);
        //        userRoleRepository.deleteUserRolesByUserId(id);
        roleRepository.deleteById(id);
    }

    @Transactional
    public RoleResponse updateRole(Integer id, RoleRequest roleRequest) {
        if (roleRepository.findById(id).isEmpty()) {
            throw new AppException(ErrorCode.ROLE_NOT_FOUND);
        }
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        roleMapper.updateRole(existingRole, roleRequest);

        //        existingRole.getRolePermissions().clear(); //Xóa cache

        // Lấy danh sách permission mới
        Set<Permission> desiredPermissions = permissionRepository.findAllByNameIn(roleRequest.getPermissions());

        // Xóa những permission cũ không có trong danh sách mới
        existingRole.getRolePermissions().removeIf(rp -> !desiredPermissions.contains(rp.getPermission()));

        // Tạo quan hệ mới
        // Thêm các permission mới chưa có
        desiredPermissions.forEach(p -> {
            boolean exists = existingRole.getRolePermissions().stream()
                    .anyMatch(rp -> rp.getPermission().equals(p));
            if (!exists) {
                existingRole.getRolePermissions().add(new RolePermission(existingRole, p));
            }
        });

        Role reloaded = roleRepository.findById(existingRole.getId()).get();

        return roleMapper.toRoleResponse(reloaded);
    }

    // Lấy danh sách Role cho create user và update user
    public List<RoleOptionResponse> getRoleOptions() {
        return roleRepository.findAll().stream()
                .map(role -> RoleOptionResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .description(role.getDescription())
                        .build())
                .toList();
    }

    @Transactional
    public RoleResponse updateRole2(Integer id, RoleRequest roleRequest) {
        Role existingRole = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
        roleMapper.updateRole(existingRole, roleRequest);
        // 1) Cập nhật các field đơn giản
        //        existingRole.setName(roleRequest.getName());
        //        existingRole.setDescription(roleRequest.getDescription());

        existingRole.getRolePermissions().clear();

        // Xóa toàn bộ role-permission cũ
        //        rolePermissionRepository.deleteRolePermissionsByRoleId(existingRole.getId());

        // Lấy danh sách permission mới
        Set<Permission> permissions = permissionRepository.findAllByNameIn(roleRequest.getPermissions());

        // Tạo quan hệ mới
        //        Set<RolePermission> rolePermissions = permissions.stream()
        //                .map(p -> new RolePermission(existingRole, p))
        //                .collect(Collectors.toSet());

        permissions.forEach(p -> existingRole
                .getRolePermissions()
                .add(RolePermission.builder().role(existingRole).permission(p).build()));

        //        rolePermissionRepository.saveAll(rolePermissions);

        //        existingRole.getRolePermissions().addAll(rolePermissions);

        //        Role reloaded = roleRepository.findById(existingRole.getId()).get();
        //        role.setName(roleRequest.getName());
        //        role.setDescription(roleRequest.getDescription());
        //        Role savedRole = roleRepository.save(role);

        roleRepository.save(existingRole);
        return roleMapper.toRoleResponse(existingRole);
    }

    @Transactional
    public RoleResponse updateRoleWrongTest(Integer id, RoleRequest roleRequest) { // đã oke
        Role existingRole =
                roleRepository.findById(id).get(); // Lấy ra role (dĩ nhiên là có permission) từ db bằng id và gán vào
        // existingRole, giả sử role
        // được lấy trong db là rtest
        // => rolePermission = {rtest p1, rtest p2} (được hibernate theo dõi và lưu lại trong cache
        roleMapper.updateRole(existingRole, roleRequest); // mapper

        //        existingRole.getRolePermissions().clear(); // clear trong cache
        rolePermissionRepository.deleteRolePermissionsByRoleId(
                id); // Tiến hành xóa rolePermission cũ trong db  => {rtest p1, rtest p2} bị xóa

        Set<Permission> permissions =
                permissionRepository.findAllByNameIn(roleRequest.getPermissions()); // Lấy danh sách permission mới
        Set<RolePermission> newRps =
                permissions.stream() // Tạo 1 Set<RolePermission> từ existingRole và permissions vừa
                        // lấy được ở trên.
                        .map(p -> new RolePermission(existingRole, p))
                        .collect(Collectors.toSet()); // giả sử newRps = {rtest p3, rtest p4} -
        // permissions mới của role
        // khác hoàn toàn với permissions cũ (p1, p2)

        rolePermissionRepository.saveAll(newRps); // lưu hàng loạt bản ghi Set<RolePermission> newRp ở trên vào
        // RolePermission
        // => lúc này

        //        existingRole.getRolePermissions().clear(); // clear trong cache
        //        existingRole.getRolePermissions().addAll(newRps); // 7
        //        roleRepository.save(existingRole); // 8
        Role reloaded = roleRepository.findById(id).get(); // 8
        return roleMapper.toRoleResponse(reloaded); // 9
    }
}
