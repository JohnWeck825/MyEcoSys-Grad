package com.example.myecosysgrad.dto.admin.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Size;

public record UserCreationRecord(
        @Size(min = 4, message = "USERNAME_INVALID") String username,
        @Size(min = 4, message = "PASSWORD_INVALID") String password,
        String firstname,
        String lastname,
        //    String email,
        LocalDate dob
        //    String phone,
        ) {}
