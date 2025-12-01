package com.rishikesh.app.service;

import com.rishikesh.app.dto.seller.SellerReqDto;
import com.rishikesh.app.dto.seller.SellerResDto;
import com.rishikesh.app.entity.ROLE;
import com.rishikesh.app.entity.SellerEntity;
import com.rishikesh.app.entity.UserEntity;
import com.rishikesh.app.repository.SellerRepo;
import com.rishikesh.app.repository.UserRepo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class SellerService {

    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;

    public SellerService(UserRepo userRepo, SellerRepo sellerRepo) {
        this.userRepo = userRepo;
        this.sellerRepo = sellerRepo;
    }

    public SellerResDto signin(@Valid SellerReqDto sellerReq,String userId) {

        SellerEntity seller = SellerEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(userId)
                .shopName(sellerReq.getShopName())
                .shopAddress(sellerReq.getShopAddress())
                .phoneNumber(sellerReq.getPhoneNumber())
                .active(true)
                .createdAt(Instant.now())
                .build();

        sellerRepo.save(seller);
        UserEntity user = userRepo.findById(userId).orElse(null);
        user.getRole().add(ROLE.SELLER);
        userRepo.save(user);
        return toResDto(seller);
    }

    private SellerResDto toResDto(SellerEntity entity){

        return SellerResDto.builder()
                .name(entity.getShopName())
                .address(entity.getShopAddress())
                .number(entity.getPhoneNumber())
                .build();
    }
}
