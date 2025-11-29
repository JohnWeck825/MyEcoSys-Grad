package com.example.myecosysgrad.dto.admin.request;

import java.math.BigDecimal;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductRequest {
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

    Integer categoryId;
    Integer brandId;
    Integer seriesId;

    Set<ProductImageRequest> images; // logic phù hợp hơn: truyền danh sách ảnh dưới dạng DTO
    Set<ProductDetailRequest> attributes; // logic phù hợp hơn: truyền thuộc tính dưới dạng DTO
}

