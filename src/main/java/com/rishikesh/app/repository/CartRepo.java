package com.rishikesh.app.repository;

import com.rishikesh.app.entity.CartEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CartRepo extends MongoRepository<CartEntity, String> {
    Optional<CartEntity> findByUserId(String userId);
}
