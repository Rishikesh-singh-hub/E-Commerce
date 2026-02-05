package com.rishikesh.user.dto;

import com.rishikesh.contracts.dto.seller.SellerReqDto;
import com.rishikesh.contracts.dto.seller.SellerResDto;
import com.rishikesh.user.entity.SellerEntity;
import com.rishikesh.contracts.dto.seller.SellerStatus;

import java.time.Instant;

public class SellerMapper {

    public static SellerEntity toEntity(SellerReqDto req, String userId) {

        SellerEntity seller = new SellerEntity();
        seller.setId(userId);
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
                .shopName(seller.getShopName())
                .shopAddress(seller.getShopAddress())
                .gstNumber(seller.getGstNumber())
                .phoneNumber(seller.getPhoneNumber())
                .status(seller.getSellerStatus())
                .createdAt(seller.getCreatedAt())
                .build();
    }

}
