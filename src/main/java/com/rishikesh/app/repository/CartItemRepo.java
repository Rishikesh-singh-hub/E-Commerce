package com.rishikesh.app.repository;

import com.rishikesh.app.entity.CartItemEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CartItemRepo extends MongoRepository<CartItemEntity,String> {
}
