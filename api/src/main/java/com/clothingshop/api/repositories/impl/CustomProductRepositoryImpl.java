package com.clothingshop.api.repositories.impl;

import com.clothingshop.api.domain.entities.ProductEntity;
import com.clothingshop.api.domain.enums.Color;
import com.clothingshop.api.domain.enums.Size;
import com.clothingshop.api.repositories.CustomProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;

import java.util.*;

public class CustomProductRepositoryImpl implements CustomProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<ProductEntity> findWithFilters(Long categoryId, Color color, Size size, Pageable pageable) {
        String query = "SELECT * FROM products";

        ArrayList<String> predicates = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();

        if (categoryId != null) {
            predicates.add("category_id = :categoryId");
            params.put("categoryId", categoryId);
        }

        if (color != null) {
            predicates.add("colors @> array[:color]");
            params.put("color", color.getEnumValue());
        }

        if (size != null) {
            predicates.add("sizes @> array[:size]");
            params.put("size", size.getEnumValue());
        }

        String predicatesQuery = String.join(" AND ", predicates);

        if (!predicatesQuery.isEmpty()) {
            query += (" WHERE " + predicatesQuery);
        }

        String countQuery = query.replaceFirst("\\*", "COUNT(*)");
        Query nativeCountQuery = entityManager.createNativeQuery(countQuery, Long.class);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            nativeCountQuery.setParameter(param.getKey(), param.getValue());
        }

        query += " ORDER BY ";

        Order[] orders = pageable.getSort().get().toArray(Order[]::new);
        List<String> orderQueries = new ArrayList<>(orders.length + 1);
        boolean hasId = false;

        for (int i = 0; i < orders.length; ++i) {
            orderQueries.add(orders[i].getProperty() + " " + orders[i].getDirection());
            if (orders[i].getProperty().equals("id")) {
                hasId = true;
            }
        }

        if (!hasId) {
            orderQueries.add("id ASC");
        }

        query += String.join(" , ", orderQueries);

        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();

        query += " LIMIT :limit";
        params.put("limit", pageSize);

        query += " OFFSET :offset";

        params.put("offset", pageSize * pageNumber);

        Query nativeQuery = entityManager.createNativeQuery(query, ProductEntity.class);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            nativeQuery.setParameter(param.getKey(), param.getValue());
        }

        List<ProductEntity> products = nativeQuery.getResultList();

        return new PageImpl<>(products, pageable, (long) nativeCountQuery.getSingleResult());
    }
}
