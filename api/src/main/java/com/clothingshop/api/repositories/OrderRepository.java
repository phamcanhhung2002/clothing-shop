package com.clothingshop.api.repositories;

import com.clothingshop.api.domain.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findByUserId(Long userId, Pageable pageable);

    @Query("SELECT o op  FROM OrderEntity o JOIN o.products op JOIN op.product p WHERE o.id = :id")
    Optional<OrderEntity> findDetailOrderById(@Param(value = "id") Long id);
}
