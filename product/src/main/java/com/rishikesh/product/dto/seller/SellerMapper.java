package com.rishikesh.product.dto.seller;

import com.rishikesh.product.entity.SellerEntity;
import com.rishikesh.product.entity.SellerStatus;

import java.time.Instant;

public class SellerMapper {

    public static SellerEntity toEntity(SellerReqDto req, String userId) {

        SellerEntity seller = new SellerEntity();
        seller.setUserId(userId);
        seller.setShopName(req.getShopName());
        seller.setGstNumber(req.getGstNumber());
        seller.setShopAddress(req.getShopAddress());
        seller.setPhoneNumber(req.getPhoneNumber());

        // system-controlled fields
        seller.setSellerStatus(SellerStatus.ACTIVE);
        seller.setCreatedAt(Instant.now());

        return seller;
    }

    public static SellerResDto toDto(SellerEntity seller) {

        return SellerResDto.builder()
                .sellerId(seller.getId())
                .shopName(seller.getShopName())
                .shopAddress(seller.getShopAddress())
                .gstNumber(seller.getGstNumber())
                .phoneNumber(seller.getPhoneNumber())
                .status(seller.getSellerStatus())
                .createdAt(seller.getCreatedAt())
                .build();
    }

}
