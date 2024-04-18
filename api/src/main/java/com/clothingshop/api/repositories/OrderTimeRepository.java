package com.clothingshop.api.repositories;

import com.clothingshop.api.domain.entities.OrderTimeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderTimeRepository extends MongoRepository<OrderTimeEntity, Long> {
}
