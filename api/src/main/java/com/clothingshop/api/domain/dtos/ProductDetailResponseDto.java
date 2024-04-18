package com.clothingshop.api.domain.dtos;

import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponseDto {
    private Long id;

    private String title;

    private String img;

    private Double price;

    private String description;

    private Boolean inStock;

    private CategoryResponseDto category;

    private Size[] sizes;

    private Color[] colors;

    private Date createdAt;

    private Date updatedAt;
}
