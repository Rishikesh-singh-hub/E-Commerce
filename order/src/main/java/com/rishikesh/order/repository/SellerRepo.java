package com.rishikesh.order.repository;

import com.rishikesh.order.entity.SellerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepo extends MongoRepository<SellerEntity, String> {
    SellerEntity findByUserId(String userId);
}
