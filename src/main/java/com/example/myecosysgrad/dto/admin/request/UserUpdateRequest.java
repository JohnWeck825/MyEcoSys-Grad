package com.example.myecosysgrad.dto.admin.request;

import java.time.LocalDate;
import java.util.Set;

import com.example.myecosysgrad.validator.DobConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    //    String id;
    String password;
    String firstname;
    String lastname;

    //    String email;

    @DobConstraint(min = 2, message = "INVALID_DOB")
    LocalDate dob;

    //    String phone;
    Set<Integer> roles;
}
