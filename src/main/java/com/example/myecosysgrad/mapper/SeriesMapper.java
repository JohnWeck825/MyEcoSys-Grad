package com.example.myecosysgrad.mapper;

import org.mapstruct.*;

import com.example.myecosysgrad.dto.admin.request.SeriesRequest;
import com.example.myecosysgrad.dto.admin.response.SeriesResponse;
import com.example.myecosysgrad.dto.admin.response.SimpleBrandResponse;
import com.example.myecosysgrad.model.Brand;
import com.example.myecosysgrad.model.Series;

@Mapper(componentModel = "spring")
public interface SeriesMapper {

    @Mapping(target = "brand", ignore = true) // logic phù hợp hơn: brand gán ở service dựa trên brandId
    Series toSeries(SeriesRequest request);

    @Mapping(target = "brand", source = "brand")
    SeriesResponse toSeriesResponse(Series series);

    default SimpleBrandResponse brandToSimpleBrandResponse(Brand b) {
        if (b == null) return null;
        return SimpleBrandResponse.builder().id(b.getId()).name(b.getName()).build();
    }

    @Mapping(target = "brand", ignore = true)
    void updateSeries(@MappingTarget Series series, SeriesRequest request);
}

