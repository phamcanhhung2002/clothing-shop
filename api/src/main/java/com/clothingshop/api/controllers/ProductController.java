package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.ProductDetailResponseDto;
import com.clothingshop.api.domain.dtos.ProductDto;
import com.clothingshop.api.domain.dtos.ProductResponseDto;
import com.clothingshop.api.domain.entities.ProductEntity;
import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import com.clothingshop.api.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;

    private ModelMapper productMapper;

    public ProductController(ProductService productService, ModelMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }

    @GetMapping
    public Page<ProductResponseDto> getProducts(@RequestParam(name = "cat", required = false) Long catId,
            @RequestParam(required = false) Color color,
            @RequestParam(name = "clothing-size", required = false) Size clothingSize,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(defaultValue = "id,asc") String[] sort) {
        List<Sort.Order> orders = new ArrayList<>();

        if (sort[0].contains(",")) {
            // will sort more than 2 fields
            // sortOrder="field, direction"
            for (String sortOrder : sort) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            // sort=[field, direction]
            orders.add(new Sort.Order(getSortDirection(sort[1]), sort[0]));
        }

        Page<ProductEntity> products = productService.findWithFilters(catId, color, clothingSize,
                PageRequest.of(page, size, Sort.by(orders)));
        return products.map(product -> productMapper.map(product, ProductResponseDto.class));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDetailResponseDto> getProduct(@PathVariable("id") Long id) {
        Optional<ProductEntity> foundProduct = productService.findOne(id);
        return foundProduct
                .map(productEntity -> new ResponseEntity<>(
                        productMapper.map(productEntity, ProductDetailResponseDto.class), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDto> createProduct(@RequestBody ProductDto productDto) {
        ProductEntity productEntity = productMapper.map(productDto, ProductEntity.class);
        return new ResponseEntity<>(
                productMapper.map(productService.createProduct(productEntity), ProductResponseDto.class),
                HttpStatus.CREATED);
    }

    @PatchMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> updateProduct(@PathVariable Long id, @RequestBody ProductDto productDto) {
        if (!productService.isExists(id)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        productService.updateProduct(id, productDto.getTitle(), productDto.getDescription(),
                productDto.getPrice(), productDto.getInStock(), productDto.getImg());
        return new ResponseEntity<>(
                null,
                HttpStatus.OK);
    }
}
