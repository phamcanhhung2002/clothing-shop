package com.clothingshop.api.domain.seed;

import com.clothingshop.api.domain.entities.CategoryEntity;
import com.clothingshop.api.domain.entities.ProductEntity;
import com.clothingshop.api.domain.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SeedData {
    CategoryEntity[] categories;
    ProductEntity[] products;
    UserEntity[] users;
}
