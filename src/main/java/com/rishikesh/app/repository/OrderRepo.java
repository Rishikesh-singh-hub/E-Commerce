package com.rishikesh.app.repository;

import com.rishikesh.app.entity.OrderEntity;
import com.rishikesh.app.entity.Status;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends MongoRepository<OrderEntity, String> {
    List<OrderEntity> findByUserIdOrderByCreatedAtDesc(String userId);
    void deleteByUserIdAndStatus(String userId, Status status);
}
