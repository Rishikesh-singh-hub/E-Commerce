package com.rishikesh.user.service;

import com.rishikesh.contracts.ROLE;
import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.repository.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternalService {

    private final UserRepo userRepo;

    public InternalService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void updateRole(String userId) {

        UserEntity entity = userRepo.findById(userId).orElse(null);
        if (entity != null) {
            List<ROLE> roles = entity.getRole();
            roles.add(ROLE.SELLER);
            entity.setRole(roles);
            userRepo.save(entity);
        }



    }
}
