package com.example.myecosysgrad.dto.admin.request;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.constraints.Size;

import com.example.myecosysgrad.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {

    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 4, message = "PASSWORD_INVALID")
    String password;

    String firstname;
    String lastname;

    //    String email;

    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;

    //    String phone;

    Set<String> roles;
}
