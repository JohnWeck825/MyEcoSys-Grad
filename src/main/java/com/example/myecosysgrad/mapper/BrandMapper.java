package com.example.myecosysgrad.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.BrandRequest;
import com.example.myecosysgrad.dto.admin.response.*;
import com.example.myecosysgrad.model.*;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    @Mapping(target = "categoryBrands", ignore = true)
    @Mapping(target = "series", ignore = true)
    Brand toBrand(BrandRequest request);

    @Mapping(target = "series", source = "series")
    @Mapping(target = "categories", source = "categoryBrands")
    BrandResponse toBrandResponse(Brand brand);

    default SimpleSeriesResponse seriesToSimpleSeriesResponse(Series s) {
        if (s == null) return null;
        return SimpleSeriesResponse.builder().id(s.getId()).name(s.getName()).slug(s.getSlug()).build();
    }

    @IterableMapping(elementTargetType = SimpleSeriesResponse.class)
    default Set<SimpleSeriesResponse> seriesToSimple(Set<Series> series) {
        if (series == null) return java.util.Collections.emptySet();
        return series.stream().map(this::seriesToSimpleSeriesResponse).collect(Collectors.toSet());
    }

    default SimpleCategoryResponse categoryBrandToSimpleCategory(CategoryBrand cb) {
        if (cb == null || cb.getCategory() == null) return null;
        Category c = cb.getCategory();
        return SimpleCategoryResponse.builder().id(c.getId()).name(c.getName()).slug(c.getSlug()).build();
    }

    @IterableMapping(elementTargetType = SimpleCategoryResponse.class)
    default Set<SimpleCategoryResponse> categoryBrandsToSimpleCategories(Set<CategoryBrand> cbs) {
        if (cbs == null) return java.util.Collections.emptySet();
        return cbs.stream().map(this::categoryBrandToSimpleCategory).collect(Collectors.toSet());
    }

    @Mapping(target = "categoryBrands", ignore = true)
    @Mapping(target = "series", ignore = true)
    void updateBrand(@MappingTarget Brand brand, BrandRequest request);
}

