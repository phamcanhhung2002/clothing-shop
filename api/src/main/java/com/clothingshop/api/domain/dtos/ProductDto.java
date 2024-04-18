package com.clothingshop.api.domain.dtos;

import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private Long id;

    private String title;

    private String description;

    private String img;

    private Double price;

    private Boolean inStock;

    private Size[] sizes;

    private Color[] colors;

    private CategoryDto category;
}
