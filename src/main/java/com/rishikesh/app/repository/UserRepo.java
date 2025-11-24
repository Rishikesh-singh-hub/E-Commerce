package com.rishikesh.app.repository;

import com.rishikesh.app.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<UserEntity,String> {
    public Optional<UserEntity> findByEmail(String email);
}
