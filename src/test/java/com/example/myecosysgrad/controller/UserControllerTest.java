package com.example.myecosysgrad.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.myecosysgrad.dto.admin.request.UserCreationRequest;
import com.example.myecosysgrad.dto.admin.response.UserResponse;
import com.example.myecosysgrad.service.admin.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:test.properties")
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserCreationRequest userCreationRequest;
    private UserResponse userResponse;
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
    }

    @Test
    void createUser_validRequest_success() throws Exception, JsonProcessingException {
        // GIVEN
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);
        when(userService.createUser(any())).thenReturn(userResponse);

        // WHEN + THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1000))
                .andExpect(jsonPath("$.result.id").value("188aca67-c30f-430b-8c04-7f657e5c2d68"));
    }

    @Test
    void createUser_usernameInvalid_fail() throws Exception, JsonProcessingException {
        // GIVEN
        userCreationRequest.setUsername("duc");
        objectMapper.registerModule(new JavaTimeModule());
        String content = objectMapper.writeValueAsString(userCreationRequest);

        // WHEN + THEN
        mockMvc.perform(MockMvcRequestBuilders.post("/admin/users/create")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(1003))
                .andExpect(jsonPath("$.message").value("Username must be at least 4 characters long"));
    }
}
