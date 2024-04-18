package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.CategoryDto;

import com.clothingshop.api.domain.dtos.CategoryResponseDto;
import com.clothingshop.api.domain.entities.CategoryEntity;
import com.clothingshop.api.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private CategoryService categoryService;

    private ModelMapper categoryMapper;

    public CategoryController(CategoryService categoryService, ModelMapper categoryMapper) {
        this.categoryService = categoryService;
        this.categoryMapper = categoryMapper;
    }

    @GetMapping
    public List<CategoryResponseDto> findALl() {
        List<CategoryEntity> categories = categoryService.findAll();
        return categories.stream().map(category -> categoryMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CategoryResponseDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryEntity categoryEntity = categoryMapper.map(categoryDto, CategoryEntity.class);
        CategoryEntity savedCategoryEntity = categoryService.save(categoryEntity);
        return new ResponseEntity<>(categoryMapper.map(savedCategoryEntity, CategoryResponseDto.class),
                HttpStatus.CREATED);
    }
}
