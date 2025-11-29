package com.example.myecosysgrad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myecosysgrad.model.Series;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Integer> {
    Optional<Series> findBySlug(String slug);
    List<Series> findByBrandId(Integer brandId);
    boolean existsByNameAndBrandId(String name, Integer brandId);
    Page<Series> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

