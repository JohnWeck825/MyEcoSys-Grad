package com.example.myecosysgrad.dto.admin.request;

// Verify token xem có đúng của hệ thống generate ra hay không

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class IntrospectRequest {
    String token;
}
