package com.rishikesh.user.repository;

import com.rishikesh.user.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserEntity,String> {
    public Optional<UserEntity> findByEmail(String email);
    public boolean existsByEmail(String email);
}
