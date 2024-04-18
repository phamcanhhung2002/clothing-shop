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
public class OrderProductDto {
    Long id;

    ProductDto product;

    Size size;

    Color color;

    Short quantity;
}
