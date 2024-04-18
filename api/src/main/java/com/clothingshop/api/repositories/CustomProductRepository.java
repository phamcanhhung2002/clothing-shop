package com.clothingshop.api.repositories;

import com.clothingshop.api.domain.entities.ProductEntity;
import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductRepository {
    Page<ProductEntity> findWithFilters(Long categoryId, Color color, Size size, Pageable pageable);
}
