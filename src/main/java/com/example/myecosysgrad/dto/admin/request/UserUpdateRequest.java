package com.example.myecosysgrad.dto.admin.request;

import java.time.LocalDate;
import java.util.Set;

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
    LocalDate dob;
    //    String phone;
    Set<Integer> roles;
}
