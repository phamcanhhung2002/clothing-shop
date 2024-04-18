package com.clothingshop.api.services.impl;

import com.clothingshop.api.domain.entities.OrderEntity;
import com.clothingshop.api.domain.entities.OrderProductEntity;
import com.clothingshop.api.domain.entities.OrderTimeEntity;
import com.clothingshop.api.repositories.OrderProductRepository;
import com.clothingshop.api.repositories.OrderRepository;
import com.clothingshop.api.services.OrderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderProductRepository orderProductRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public OrderEntity createOrder(OrderEntity order) {
        OrderProductEntity[] orderProducts = order.getProducts().toArray(OrderProductEntity[]::new);
        order.setProducts(null);

        for (OrderProductEntity orderProduct : orderProducts) {
            orderProduct.setOrder(order);
        }

        OrderEntity savedOrder = orderRepository.save(order);

        for (OrderProductEntity orderProduct : orderProducts) {
            orderProductRepository.save(orderProduct);
        }

        OrderTimeEntity orderTimeEntity = OrderTimeEntity.builder()
                .orderId(savedOrder.getId())
                .createdAt(savedOrder.getCreatedAt())
                .userId(savedOrder.getUser().getId())
                .build();

        mongoTemplate.insert(orderTimeEntity);

        return savedOrder;
    }

    @Override
    public Page<OrderEntity> findUserOrders(long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Optional<OrderEntity> findDetailOrderById(Long id) {
        return orderRepository.findDetailOrderById(id);
    }

    @Override
    public Page<OrderEntity> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }
}
