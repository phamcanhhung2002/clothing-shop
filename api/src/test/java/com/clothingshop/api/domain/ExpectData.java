package com.clothingshop.api.domain;

import com.clothingshop.api.domain.dtos.OrderStatResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpectData {
    OrderStatResponseDto[] expect;
}
