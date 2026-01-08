package com.rishikesh.cart.repository;

import com.rishikesh.cart.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepo extends MongoRepository<CartEntity, String> {
    Optional<CartEntity> findByUserId(String userId);
}
