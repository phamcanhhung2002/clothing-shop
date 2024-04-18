package com.clothingshop.api.domain.dtos;

import com.clothingshop.api.domain.enums.Interval;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderFilterDto {
    Date startDate;
    Date endDate;
    Interval interval;
    Long userId;
}
