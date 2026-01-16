package com.rishikesh.product.service;

import com.rishikesh.product.dto.seller.SellerMapper;
import com.rishikesh.product.dto.seller.SellerReqDto;
import com.rishikesh.product.dto.seller.SellerResDto;
import com.rishikesh.product.entity.SellerEntity;
import com.rishikesh.product.repository.SellerRepo;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private final SellerRepo sellerRepo;

    public SellerService(SellerRepo sellerRepo) {
        this.sellerRepo = sellerRepo;
    }

    public SellerResDto addSeller(String userId, SellerReqDto sellerReqDto){

        SellerEntity seller = SellerMapper.toEntity(sellerReqDto,userId);
        sellerRepo.save(seller);
        return SellerMapper.toDto(seller);


    }


}
