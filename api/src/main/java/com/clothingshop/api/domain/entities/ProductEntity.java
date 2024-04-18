package com.clothingshop.api.domain.entities;

import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.array.IntArrayType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
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
@Table(name = "products")
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String img;

    private Double price;

    private Boolean inStock;

    @Basic
    @Type(IntArrayType.class)
    @Column(name = "sizes", columnDefinition = "smallint[]")
    private short[] sizeEnumValues;

    @Transient
    private Size[] sizes;

    @Basic
    @Type(IntArrayType.class)
    @Column(name = "colors", columnDefinition = "smallint[]")
    private short[] colorEnumValues;

    @Transient
    private Color[] colors;

    @PostLoad
    void fillTransient() {
        this.colors = new Color[colorEnumValues.length];

        for (int i = 0; i < colorEnumValues.length; ++i) {
            this.colors[i] = Color.of(colorEnumValues[i]);
        }

        this.sizes = new Size[sizeEnumValues.length];

        for (int i = 0; i < sizeEnumValues.length; ++i) {
            this.sizes[i] = Size.of(sizeEnumValues[i]);
        }
    }

    @PrePersist
    void fillPersistent() {
        this.colorEnumValues = new short[colors.length];
        for (int i = 0; i < colors.length; ++i) {
            this.colorEnumValues[i] = colors[i].getEnumValue();
        }

        this.sizeEnumValues = new short[sizes.length];
        for (int i = 0; i < sizes.length; ++i) {
            this.sizeEnumValues[i] = sizes[i].getEnumValue();
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @JsonBackReference(value = "category-product")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product")
    @JsonManagedReference(value = "product-orderproduct")
    List<OrderProductEntity> orders;

    @CreatedDate
    private Date createdAt;

    @LastModifiedDate
    private Date updatedAt;
}
