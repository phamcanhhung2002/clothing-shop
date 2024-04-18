package com.clothingshop.api.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponseDto {
    private Long id;

    private String title;

    private String img;

    private String cat;

    private Date createdAt;

    private Date updatedAt;
}
