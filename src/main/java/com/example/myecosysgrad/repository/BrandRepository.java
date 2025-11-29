package com.example.myecosysgrad.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.myecosysgrad.model.Brand;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {
    boolean existsByName(String name);
    Optional<Brand> findByName(String name);
    Page<Brand> findByNameContainingIgnoreCase(String name, Pageable pageable);
}

