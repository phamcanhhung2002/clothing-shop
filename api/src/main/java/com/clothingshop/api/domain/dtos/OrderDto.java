package com.clothingshop.api.domain.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDto {
    private Long id;

    private UserDto user;

    private Double amount;

    private String city;

    private String country;

    private String line1;

    @JsonAlias("postal_code")
    private String postalCode;

    private OrderProductDto[] products;
}
