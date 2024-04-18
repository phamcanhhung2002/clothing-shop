package com.clothingshop.api.domain.dtos;

import com.clothingshop.api.domain.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDto {
    Long id;
    Double amount;
    UserResponseDto user;
    OrderStatus status;
    Date createdAt;
}
