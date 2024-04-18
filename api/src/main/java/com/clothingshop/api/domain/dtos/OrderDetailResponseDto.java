package com.clothingshop.api.domain.dtos;

import com.clothingshop.api.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailResponseDto {
    private Long id;

    private UserResponseDto user;

    private Double amount;

    private OrderStatus status;

    private String city;

    private String country;

    private String line1;

    private String line2;

    private String postalCode;

    private String state;

    private List<OrderProductResponseDto> products;

    private Date createdAt;

    private Date updatedAt;
}
