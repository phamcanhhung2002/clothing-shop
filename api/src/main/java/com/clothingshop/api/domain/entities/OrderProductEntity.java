package com.clothingshop.api.domain.entities;

import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "order_products", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "order_id", "product_id", "size", "color" })
})
public class OrderProductEntity {
    @Id
    @GeneratedValue
    Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    @JsonBackReference(value = "order-orderproduct")
    OrderEntity order;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    @JsonBackReference(value = "product-orderproduct")
    ProductEntity product;

    @Enumerated(EnumType.ORDINAL)
    Color color;

    @Enumerated(EnumType.ORDINAL)
    Size size;

    Short quantity;
}
