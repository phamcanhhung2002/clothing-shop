package com.clothingshop.api.services;

import com.clothingshop.api.domain.entities.CategoryEntity;

import java.util.List;

public interface CategoryService {

    List<CategoryEntity> findAll();

    CategoryEntity save(CategoryEntity categoryEntity);
}
