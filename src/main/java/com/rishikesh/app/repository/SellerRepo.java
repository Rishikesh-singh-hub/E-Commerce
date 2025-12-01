package com.rishikesh.app.repository;

import com.rishikesh.app.entity.SellerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SellerRepo extends MongoRepository<SellerEntity,String> {
}
