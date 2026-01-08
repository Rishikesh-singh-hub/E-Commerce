package com.rishikesh.product.repository;

import com.rishikesh.product.entity.SellerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepo extends MongoRepository<SellerEntity, String> {
    SellerEntity findByUserId(String userId);
}
