package com.clothingshop.api.domain.entities;

import com.clothingshop.api.domain.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class OrderEntity {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference(value = "user-order")
    private UserEntity user;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    private String city;

    private String country;

    private String line1;

    private String postalCode;

    @OneToMany(mappedBy = "order")
    @JsonManagedReference(value = "order-orderproduct")
    List<OrderProductEntity> products;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
