package com.clothingshop.api.controllers;

import com.clothingshop.api.domain.dtos.OrderDetailResponseDto;
import com.clothingshop.api.domain.dtos.OrderResponseDto;
import com.clothingshop.api.domain.entities.OrderEntity;
import com.clothingshop.api.domain.entities.UserEntity;
import com.clothingshop.api.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/orders")
    @PreAuthorize("hasRole('USER')")
    public Page<OrderResponseDto> findUserOrders(@AuthenticationPrincipal UserEntity userEntity, Pageable pageable) {
        Page<OrderEntity> orders = orderService.findUserOrders(userEntity.getId(), pageable);
        return orders.map(orderEntity -> modelMapper.map(orderEntity, OrderResponseDto.class));
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDetailResponseDto> findUserOrder(@AuthenticationPrincipal UserEntity user,
            @PathVariable Long id) {
        Optional<OrderEntity> foundOrder = orderService.findDetailOrderById(id);

        return foundOrder.map(orderEntity -> {
            if (!orderEntity.getUser().getId().equals(user.getId())) {
                return new ResponseEntity<OrderDetailResponseDto>(HttpStatus.FORBIDDEN);
            }

            OrderDetailResponseDto orderDetailResponseDto = modelMapper.map(orderEntity, OrderDetailResponseDto.class);

            return new ResponseEntity<>(orderDetailResponseDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
