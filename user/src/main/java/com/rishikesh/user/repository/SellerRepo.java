package com.rishikesh.user.repository;

import com.rishikesh.user.entity.SellerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepo extends MongoRepository<SellerEntity, String> {
}
