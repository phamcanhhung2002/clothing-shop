package com.clothingshop.api.repositories;

import com.clothingshop.api.domain.entities.ProductEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, CustomProductRepository {
    @Modifying
    @Transactional
    @Query("update ProductEntity p set p.title = :title, p.description = :description, p.price = :price, p.inStock = :inStock, p.img = :img where p.id = :id")
    void updateProduct(@Param(value = "id") long id,
            @Param(value = "title") String title,
            @Param(value = "description") String description,
            @Param(value = "price") Double price,
            @Param(value = "inStock") Boolean inStock,
            @Param(value = "img") String img);
}
