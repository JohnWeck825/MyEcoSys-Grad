package com.example.myecosysgrad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.myecosysgrad.model.ProductDetail;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Integer> {
    List<ProductDetail> findByProductId(Integer productId);

    @Modifying
    @Query("DELETE FROM ProductDetail pd WHERE pd.product.id = :productId")
    void deleteByProductId(Integer productId);
}

