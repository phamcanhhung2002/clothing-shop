package com.clothingshop.api.services;

import com.clothingshop.api.domain.dtos.OrderStatResponseDto;
import com.clothingshop.api.domain.enums.Interval;

import java.util.Date;
import java.util.List;

public interface StatService {
    List<OrderStatResponseDto> getOrderStat(Date fromDate, Date toDate, String timeZone, Long userId,
            Interval interval);

    void seed() throws Exception;
}
