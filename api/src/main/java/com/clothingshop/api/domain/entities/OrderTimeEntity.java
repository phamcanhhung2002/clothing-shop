package com.clothingshop.api.domain.entities;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("orders")
public class OrderTimeEntity {
    @Id
    Long orderId;

    Date createdAt;

    Long userId;
}
