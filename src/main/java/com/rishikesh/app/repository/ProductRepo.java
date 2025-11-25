package com.rishikesh.app.repository;


import com.rishikesh.app.entity.ProductEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepo extends MongoRepository<ProductEntity, String> {
}
