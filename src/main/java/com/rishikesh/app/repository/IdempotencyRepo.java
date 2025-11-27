package com.rishikesh.app.repository;


import com.rishikesh.app.entity.IdempotencyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface IdempotencyRepo extends MongoRepository<IdempotencyRecord, String> {
   Optional<IdempotencyRecord> findByUserIdAndIdempotencyKey(String userId, String idempotencyKey);
}
