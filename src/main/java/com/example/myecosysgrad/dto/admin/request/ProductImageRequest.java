package com.example.myecosysgrad.dto.admin.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageRequest {
    String imageUrl;
    Integer position;
    Boolean isMain;
}

