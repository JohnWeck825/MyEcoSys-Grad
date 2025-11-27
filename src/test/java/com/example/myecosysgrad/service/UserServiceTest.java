package com.example.myecosysgrad.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import com.example.myecosysgrad.dto.admin.request.UserCreationRequest;
import com.example.myecosysgrad.dto.admin.response.UserResponse;
import com.example.myecosysgrad.exception.AppException;
import com.example.myecosysgrad.model.User;
import com.example.myecosysgrad.repository.RoleRepository;
import com.example.myecosysgrad.repository.UserRepository;
import com.example.myecosysgrad.service.admin.UserService;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:test.properties")
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserCreationRequest userCreationRequest;

    @Mock
    private UserResponse userResponse;

    @Mock
    private User user;

    @Mock
    private LocalDate dob;

    @BeforeEach
    void initData() {
        dob = LocalDate.of(2003, 10, 24);

        userCreationRequest = UserCreationRequest.builder()
                .username("test")
                .firstname("nam")
                .lastname("duc")
                .password("1234")
                .dob(dob)
                .build();

        userResponse = UserResponse.builder()
                .id("188aca67-c30f-430b-8c04-7f657e5c2d68")
                .username("test")
                .firstname("nam")
                .lastname("duc")
                .dob(dob)
                .build();

        user = User.builder()
                .id("188aca67-c30f-430b-8c04-7f657e5c2d68")
                .username("test")
                .firstname("nam")
                .lastname("duc")
                .dob(dob)
                .build();
    }

    @Test
    void createUser_validRequest_success() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(user);
        //
        // when(userRepository.findById("188aca67-c30f-430b-8c04-7f657e5c2d68")).thenReturn(Optional.of(user));

        when(userRepository.save(any())).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId("188aca67-c30f-430b-8c04-7f657e5c2d68");
            return u;
        });

        // WHEN
        userService.createUser(userCreationRequest);

        // THEN
        assertThat(userResponse.getId()).isEqualTo("188aca67-c30f-430b-8c04-7f657e5c2d68");
        assertThat(userResponse.getUsername()).isEqualTo("test");
    }

    @Test
    void createUser_userExisted_fail() {
        // GIVEN
        when(userRepository.existsByUsername(anyString())).thenReturn(true);

        // WHEN
        var exception = assertThrows(AppException.class, () -> userService.createUser(userCreationRequest));

        // THEN
        assertThat(exception.getErrorCode().getCode()).isEqualTo(1002);
    }
}
