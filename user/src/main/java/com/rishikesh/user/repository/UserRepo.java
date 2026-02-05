package com.rishikesh.user.repository;

import com.rishikesh.user.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserEntity,String> {
     Optional<UserEntity> findByEmail(String email);
     boolean existsByEmail(String email);
}
