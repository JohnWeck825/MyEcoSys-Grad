package com.example.myecosysgrad.dto.admin.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SimpleUserResponse {
    String username;
    //    String firstname;
    //    String lastname;
}
