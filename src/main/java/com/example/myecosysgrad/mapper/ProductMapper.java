package com.example.myecosysgrad.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.ProductRequest;
import com.example.myecosysgrad.dto.admin.response.*;
import com.example.myecosysgrad.model.*;

@Mapper(componentModel = "spring", uses = {ProductImageMapper.class, ProductDetailMapper.class})
public interface ProductMapper {

    @Mapping(target = "category", ignore = true) // logic phù hợp hơn: gán quan hệ ở service
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "series", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    Product toProduct(ProductRequest request);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "brand", source = "brand")
    @Mapping(target = "series", source = "series")
    @Mapping(target = "images", source = "images")
    @Mapping(target = "attributes", source = "attributes")
    ProductResponse toProductResponse(Product product);

    default SimpleCategoryResponse categoryToSimpleCategoryResponse(Category c) {
        if (c == null) return null;
        return SimpleCategoryResponse.builder().id(c.getId()).name(c.getName()).slug(c.getSlug()).build();
    }

    default SimpleBrandResponse brandToSimpleBrandResponse(Brand b) {
        if (b == null) return null;
        return SimpleBrandResponse.builder().id(b.getId()).name(b.getName()).build();
    }

    default SimpleSeriesResponse seriesToSimpleSeriesResponse(Series s) {
        if (s == null) return null;
        return SimpleSeriesResponse.builder().id(s.getId()).name(s.getName()).slug(s.getSlug()).build();
    }

    @IterableMapping(elementTargetType = SimpleProductImageResponse.class)
    default Set<SimpleProductImageResponse> imagesToSimple(Set<ProductImage> images) {
        if (images == null) return java.util.Collections.emptySet();
        return images.stream()
                .map(i -> SimpleProductImageResponse.builder()
                        .id(i.getId())
                        .imageUrl(i.getImageUrl())
                        .position(i.getPosition())
                        .isMain(i.getIsMain())
                        .build())
                .collect(Collectors.toSet());
    }

    @IterableMapping(elementTargetType = SimpleProductDetailResponse.class)
    default Set<SimpleProductDetailResponse> attributesToSimple(Set<ProductDetail> attrs) {
        if (attrs == null) return java.util.Collections.emptySet();
        return attrs.stream()
                .map(a -> SimpleProductDetailResponse.builder()
                        .id(a.getId())
                        .name(a.getName())
                        .value(a.getValue())
                        .build())
                .collect(Collectors.toSet());
    }

    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "series", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    void updateProduct(@MappingTarget Product product, ProductRequest request);
}

