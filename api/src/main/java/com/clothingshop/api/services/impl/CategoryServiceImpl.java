package com.clothingshop.api.services.impl;

import com.clothingshop.api.domain.entities.CategoryEntity;
import com.clothingshop.api.repositories.CategoryRepository;
import com.clothingshop.api.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class CategoryServiceImpl implements CategoryService {
    private CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<CategoryEntity> findAll() {
        return StreamSupport.stream(categoryRepository
                .findAll()
                .spliterator(),
                false)
                .collect(Collectors.toList());
    }

    public CategoryEntity save(CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }
}
