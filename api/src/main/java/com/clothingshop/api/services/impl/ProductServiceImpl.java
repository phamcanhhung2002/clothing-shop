package com.clothingshop.api.services.impl;

import com.clothingshop.api.domain.entities.ProductEntity;
import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import com.clothingshop.api.repositories.ProductRepository;
import com.clothingshop.api.services.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ProductEntity> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public ProductEntity createProduct(ProductEntity productEntity) {
        return productRepository.save(productEntity);
    }

    @Override
    public Optional<ProductEntity> findOne(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<ProductEntity> findWithFilters(Long categoryId, Color color, Size size, Pageable pageable) {
        return productRepository.findWithFilters(categoryId, color, size, pageable);
    }

    @Override
    public void updateProduct(Long id, String title, String description, Double price, Boolean inStock, String img) {
        productRepository.updateProduct(id, title, description, price, inStock, img);
    }

    @Override
    public boolean isExists(Long id) {
        return productRepository.existsById(id);
    }
}
