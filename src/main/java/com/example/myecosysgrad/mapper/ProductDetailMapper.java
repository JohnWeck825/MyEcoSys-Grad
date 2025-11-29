package com.example.myecosysgrad.mapper;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.ProductDetailRequest;
import com.example.myecosysgrad.dto.admin.response.SimpleProductDetailResponse;
import com.example.myecosysgrad.model.ProductDetail;

@Mapper(componentModel = "spring")
public interface ProductDetailMapper {

    @Mapping(target = "product", ignore = true) // logic phù hợp hơn: gán product ở service
    ProductDetail toProductDetail(ProductDetailRequest request);

    SimpleProductDetailResponse toSimpleProductDetailResponse(ProductDetail detail);

    @Mapping(target = "product", ignore = true)
    void updateProductDetail(@MappingTarget ProductDetail detail, ProductDetailRequest request);
}

