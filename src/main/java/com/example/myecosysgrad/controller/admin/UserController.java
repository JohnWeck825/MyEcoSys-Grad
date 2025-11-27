package com.example.myecosysgrad.controller.admin;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.myecosysgrad.dto.admin.request.UserCreationRequest;
import com.example.myecosysgrad.dto.admin.request.UserUpdateRequest;
import com.example.myecosysgrad.dto.admin.response.UserResponse;
import com.example.myecosysgrad.dto.api.request.ApiResponse;
import com.example.myecosysgrad.service.admin.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @GetMapping()
    ApiResponse<List<UserResponse>> getUsers() {
        //        var authentication = SecurityContextHolder.getContext().getAuthentication();
        //        log.info("Username: {}", authentication.getName());
        //        authentication.getAuthorities().forEach(grantedAuthority -> log.info("Role: {}",
        // grantedAuthority.getAuthority()));

        //        return ApiResponse.<List<UserResponse>>builder()
        //                .result(userService.getUsers())
        //                .build();
        return ApiResponse.success(userService.getUsers());
    }

    @PostMapping("/create")
    ApiResponse<UserResponse> create(@RequestBody @Valid UserCreationRequest request) {
        //        return ApiResponse.<UserResponse>builder()
        //                .result(userService.createUser(request))
        //                .build();
        return ApiResponse.success(userService.createUser(request));
    }

    @GetMapping("/{id}")
    ApiResponse<UserResponse> getUser(@PathVariable("id") String id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @GetMapping("/myInfo")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@PathVariable("id") String id, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(id, request))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<String> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder().result("User deleted successfully").build();
    }
}
