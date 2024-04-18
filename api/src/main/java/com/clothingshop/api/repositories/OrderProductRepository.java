package com.clothingshop.api.repositories;

import com.clothingshop.api.domain.entities.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
}
