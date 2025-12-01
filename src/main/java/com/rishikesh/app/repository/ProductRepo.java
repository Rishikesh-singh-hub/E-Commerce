package com.rishikesh.app.repository;


import com.rishikesh.app.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends MongoRepository<ProductEntity, String> {
    Optional<ProductEntity> findByIdAndStockGreaterThanEqual(String id, int qty);
    List<ProductEntity> findAllByNameContainingIgnoreCase(String name);
}
