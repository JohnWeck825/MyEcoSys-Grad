package com.example.myecosysgrad.dto.admin.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeriesRequest {
    String name;
    String slug;
    Boolean active;
    Integer brandId; // logic phù hợp hơn: chỉ nhận brandId, gán quan hệ ở service
}

