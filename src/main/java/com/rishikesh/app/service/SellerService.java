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
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Service
public class SellerService {

    private final UserRepo userRepo;
    private final SellerRepo sellerRepo;

    public SellerService(UserRepo userRepo, SellerRepo sellerRepo) {
        this.userRepo = userRepo;
        this.sellerRepo = sellerRepo;
    }

    public SellerResDto signin(@Valid SellerReqDto sellerReq,String userId) {

        SellerEntity.SellerEntityBuilder sellerBuilder = SellerEntity.builder()
            .id(UUID.randomUUID().toString())
            .userId(userId)
            .shopName(sellerReq.getShopName())
            .shopAddress(sellerReq.getShopAddress())
            .phoneNumber(sellerReq.getPhoneNumber())
            .active(true)
            .createdAt(Instant.now());

        // set location if provided (GeoJsonPoint expects lon, lat)
        if (sellerReq.getLatitude() != null && sellerReq.getLongitude() != null) {
            GeoJsonPoint p = new GeoJsonPoint(sellerReq.getLongitude(), sellerReq.getLatitude());
            sellerBuilder.location(p);
        }

        SellerEntity seller = sellerBuilder.build();
        sellerRepo.save(seller);
        UserEntity user = userRepo.findById(userId).orElse(null);
        user.getRole().add(ROLE.SELLER);
        userRepo.save(user);
        return toResDto(seller);
    }

    private SellerResDto toResDto(SellerEntity entity){

        SellerResDto.SellerResDtoBuilder builder = SellerResDto.builder()
                .name(entity.getShopName())
                .address(entity.getShopAddress())
                .number(entity.getPhoneNumber());

        if (entity.getLocation() != null) {
            // GeoJsonPoint stores X=lon, Y=lat
            builder.latitude(entity.getLocation().getY());
            builder.longitude(entity.getLocation().getX());
        }

        return builder.build();
    }
}
