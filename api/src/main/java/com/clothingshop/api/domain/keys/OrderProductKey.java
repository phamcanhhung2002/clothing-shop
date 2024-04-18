package com.clothingshop.api.domain.keys;

import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Embeddable
public class OrderProductKey implements Serializable {
    @Column(name = "order_id")
    Long orderId;

    @Column(name = "product_id")
    Long productId;

    @Column
    @Enumerated(EnumType.ORDINAL)
    Size size;

    @Column
    @Enumerated(EnumType.ORDINAL)
    Color color;

    @Override
    public String toString() {
        return orderId.toString() + productId.toString() + size.toString() + color.toString();
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof OrderProductKey orderProductKey) {
            return orderId.equals(orderProductKey.orderId) &&
                    productId.equals(orderProductKey.productId) &&
                    size == orderProductKey.size &&
                    color == orderProductKey.color;

        }

        return false;
    }
}
