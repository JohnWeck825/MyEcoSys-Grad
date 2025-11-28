package com.example.myecosysgrad.service.admin;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.myecosysgrad.dto.admin.request.UserCreationRequest;
import com.example.myecosysgrad.dto.admin.request.UserUpdateRequest;
import com.example.myecosysgrad.dto.admin.response.UserResponse;
import com.example.myecosysgrad.exception.AppException;
import com.example.myecosysgrad.exception.ErrorCode;
import com.example.myecosysgrad.mapper.UserMapper;
import com.example.myecosysgrad.model.Role;
import com.example.myecosysgrad.model.User;
import com.example.myecosysgrad.model.UserRole;
import com.example.myecosysgrad.repository.RoleRepository;
import com.example.myecosysgrad.repository.UserRepository;
import com.example.myecosysgrad.repository.UserRoleRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    UserRoleRepository userRoleRepository;

    //    PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse createUser(UserCreationRequest request) {
        try {
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.USER_EXISTED);
            }

            User user = userMapper.toUser(request);
            User savedUser = userRepository.save(user);

            //        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            Set<Role> roles = roleRepository.findAllByIdIn(request.getRoles());

            Set<UserRole> userRoles =
                    roles.stream().map(r -> new UserRole(savedUser, r)).collect(Collectors.toSet());
            userRoleRepository.saveAll(userRoles);
            savedUser.setUserRoles(userRoles);
            User reloaded = userRepository
                    .findById(savedUser.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            return userMapper.toUserResponse(reloaded);
        } catch (DataIntegrityViolationException exception) {
            // Xử lý trường hợp trùng username do race condition
            if (userRepository.existsByUsername(request.getUsername())) {
                throw new AppException(ErrorCode.USER_EXISTED);
            }
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        log.info("User: {}", user);
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('CREATE_DATA')")
    public List<UserResponse> getUsers() {
        log.info("In method getUsers");
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    // @PreAuthorize xảy ra trước hàm, @PostAuthorize xảy ra sau hàm

    //    @PostAuthorize("returnObject.username == authentication.name || hasRole('ADMIN')")
    // nếu đã đăng nhập, sẽ chỉ lấy được thông tin của chính mình  hoặc có quyền admin
    public UserResponse getUserById(String id) { // returnObject = UserResponse (returnObject.username =
        // UserResponse.username)
        //        log.info("In method getUserById");
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
    }

    @Transactional
    public UserResponse updateUser(String id, UserUpdateRequest request) {
        try {
            User existingUser =
                    userRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            userMapper.updateUser(existingUser, request);
            existingUser.setPassword(passwordEncoder.encode(request.getPassword()));

            //        existingUser.getUserRoles().clear();

            Set<Role> desiredRoles = roleRepository.findAllByIdIn(request.getRoles());
            existingUser.getUserRoles().removeIf(ur -> !desiredRoles.contains(ur.getRole()));
            desiredRoles.forEach(r -> {
                if (existingUser.getUserRoles().stream()
                        .noneMatch(ur -> ur.getRole().equals(r))) {
                    existingUser.getUserRoles().add(new UserRole(existingUser, r));
                }
            });

            if (request.getPassword() != null && !request.getPassword().isBlank()) {
                existingUser.setPassword(passwordEncoder.encode(request.getPassword()));
            }

            User reloaded = userRepository
                    .findById(existingUser.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            return userMapper.toUserResponse(reloaded);
        } catch (Exception e) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}
