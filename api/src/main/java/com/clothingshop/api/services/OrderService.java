package com.clothingshop.api.services;

import com.clothingshop.api.domain.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface OrderService {
    OrderEntity createOrder(OrderEntity order);

    Page<OrderEntity> findUserOrders(long id, Pageable pageable);

    Optional<OrderEntity> findDetailOrderById(Long id);

    Page<OrderEntity> findAll(Pageable pageable);
}
