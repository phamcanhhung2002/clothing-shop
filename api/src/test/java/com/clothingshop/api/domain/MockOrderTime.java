package com.clothingshop.api.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MockOrderTime {
    Long orderId;

    String createdAt;

    Long userId;
}
