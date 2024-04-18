package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.OrderDto;
import com.clothingshop.api.domain.dtos.OrderDetailResponseDto;
import com.clothingshop.api.domain.dtos.OrderResponseDto;
import com.clothingshop.api.domain.entities.OrderEntity;
import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapper orderMapper;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDetailResponseDto> createOrder(@AuthenticationPrincipal UserEntity user,
            @RequestBody OrderDto orderDto) {
        OrderEntity order = orderMapper.map(orderDto, OrderEntity.class);
        order.setUser(user);

        OrderEntity savedOrder = orderService.createOrder(order);
        return new ResponseEntity<>(orderMapper.map(savedOrder, OrderDetailResponseDto.class), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Page<OrderResponseDto> findOrders(Pageable pageable, Sort sort) {
        Pageable pagingWithSorting = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        Page<OrderEntity> orders = orderService.findAll(pagingWithSorting);
        return orders.map(orderEntity -> orderMapper.map(orderEntity, OrderResponseDto.class));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderDetailResponseDto> findOrder(@PathVariable Long id) {
        Optional<OrderEntity> foundOrder = orderService.findDetailOrderById(id);

        return foundOrder.map(orderEntity -> {
            OrderDetailResponseDto orderDetailResponseDto = orderMapper.map(orderEntity, OrderDetailResponseDto.class);

            return new ResponseEntity<>(orderDetailResponseDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
