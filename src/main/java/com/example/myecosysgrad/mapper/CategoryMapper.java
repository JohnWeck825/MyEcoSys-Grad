package com.example.myecosysgrad.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.CategoryRequest;
import com.example.myecosysgrad.dto.admin.response.*;
import com.example.myecosysgrad.model.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parent", ignore = true) // logic phù hợp hơn: gán parent ở service theo parentId
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "categoryBrands", ignore = true)
    Category toCategory(CategoryRequest request);

    @Mapping(target = "parent", source = "parent")
    @Mapping(target = "subCategories", source = "subCategories")
    @Mapping(target = "brands", source = "categoryBrands")
    CategoryResponse toCategoryResponse(Category category);

    default SimpleCategoryResponse categoryToSimpleCategoryResponse(Category category) {
        if (category == null) return null;
        return SimpleCategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .slug(category.getSlug())
                .build();
    }

    @IterableMapping(elementTargetType = SimpleCategoryResponse.class)
    default Set<SimpleCategoryResponse> categoriesToSimple(Set<Category> categories) {
        if (categories == null) return java.util.Collections.emptySet();
        return categories.stream().map(this::categoryToSimpleCategoryResponse).collect(Collectors.toSet());
    }

    default SimpleBrandResponse categoryBrandToSimpleBrand(CategoryBrand cb) {
        if (cb == null || cb.getBrand() == null) return null;
        return SimpleBrandResponse.builder()
                .id(cb.getBrand().getId())
                .name(cb.getBrand().getName())
                .build();
    }

    @IterableMapping(elementTargetType = SimpleBrandResponse.class)
    default Set<SimpleBrandResponse> categoryBrandsToSimpleBrands(Set<CategoryBrand> cbs) {
        if (cbs == null) return java.util.Collections.emptySet();
        return cbs.stream().map(this::categoryBrandToSimpleBrand).collect(Collectors.toSet());
    }

    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    @Mapping(target = "categoryBrands", ignore = true)
    void updateCategory(@MappingTarget Category category, CategoryRequest request);
}

