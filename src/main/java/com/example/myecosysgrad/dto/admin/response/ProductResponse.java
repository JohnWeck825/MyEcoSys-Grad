package com.example.myecosysgrad.dto.admin.response;

import java.math.BigDecimal;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductResponse {
    Integer id;
    String slug;
    String name;
    BigDecimal price;
    BigDecimal listPrice;
    String descriptionContent;
    String descriptionTitle;
    String mainImageUrl;
    Boolean active;
    Boolean inStock;
    BigDecimal discount;

    SimpleCategoryResponse category;
    SimpleBrandResponse brand;
    SimpleSeriesResponse series;
    Set<SimpleProductImageResponse> images;
    Set<SimpleProductDetailResponse> attributes;
}

