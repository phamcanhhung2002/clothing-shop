package com.clothingshop.api.services;

import com.clothingshop.api.domain.entities.ProductEntity;
import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ProductService {

    Page<ProductEntity> findAll(Pageable pageable);

    ProductEntity createProduct(ProductEntity productEntity);

    Optional<ProductEntity> findOne(Long id);

    Page<ProductEntity> findWithFilters(Long categoryId, Color color, Size size, Pageable pageable);

    void updateProduct(Long id, String title, String description, Double price, Boolean inStock, String img);

    boolean isExists(Long id);
}
