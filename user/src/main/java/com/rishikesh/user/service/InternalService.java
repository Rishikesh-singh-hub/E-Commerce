package com.rishikesh.user.service;

import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.repository.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class InternalService {

    private final UserRepo userRepo;

    public InternalService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UserEntity getById(String userId){
        return userRepo.findById(userId).orElse(null);
    }

}
