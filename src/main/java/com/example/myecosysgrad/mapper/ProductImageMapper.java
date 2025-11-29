package com.example.myecosysgrad.mapper;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.ProductImageRequest;
import com.example.myecosysgrad.dto.admin.response.SimpleProductImageResponse;
import com.example.myecosysgrad.model.ProductImage;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(target = "product", ignore = true) // logic phù hợp hơn: gán product ở service
    ProductImage toProductImage(ProductImageRequest request);

    SimpleProductImageResponse toSimpleProductImageResponse(ProductImage image);

    @Mapping(target = "product", ignore = true)
    void updateProductImage(@MappingTarget ProductImage image, ProductImageRequest request);
}

