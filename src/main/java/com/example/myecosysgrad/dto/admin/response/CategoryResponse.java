package com.example.myecosysgrad.dto.admin.response;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryResponse {
    Integer id;
    String name;
    String photoUrl;
    String slug;
    Boolean active;
    SimpleCategoryResponse parent;
    Set<SimpleCategoryResponse> subCategories;
    Set<SimpleBrandResponse> brands; // lấy từ CategoryBrand
}

