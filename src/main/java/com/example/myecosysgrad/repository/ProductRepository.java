package com.example.myecosysgrad.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myecosysgrad.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySlug(String slug);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Product> findByCategoryId(Integer categoryId);
    List<Product> findByBrandId(Integer brandId);
    List<Product> findBySeriesId(Integer seriesId);
}

