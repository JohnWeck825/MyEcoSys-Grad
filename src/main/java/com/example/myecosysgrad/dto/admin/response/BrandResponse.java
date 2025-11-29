package com.example.myecosysgrad.dto.admin.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BrandResponse {
    Integer id;
    String name;
    String logo;
    Boolean active;
    Set<SimpleSeriesResponse> series;
    Set<SimpleCategoryResponse> categories; // lấy từ CategoryBrand
}

