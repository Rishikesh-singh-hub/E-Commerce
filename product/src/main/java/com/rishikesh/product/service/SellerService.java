package com.rishikesh.product.service;

import com.rishikesh.product.dto.seller.SellerMapper;
import com.rishikesh.product.dto.seller.SellerReqDto;
import com.rishikesh.product.dto.seller.SellerResDto;
import com.rishikesh.product.entity.SellerEntity;
import com.rishikesh.product.repository.SellerRepo;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Lazy
public class SellerService {

    private final SellerRepo sellerRepo;
    private final WebClient webClient;

    public SellerService(SellerRepo sellerRepo, WebClient.Builder webClient) {
        this.sellerRepo = sellerRepo;
        this.webClient = webClient
                .baseUrl("http://localhost:8080/internal/user/")
                .build();
    }

    public SellerResDto addSeller(String userId, SellerReqDto sellerReqDto){

        SellerEntity seller = SellerMapper.toEntity(sellerReqDto,userId);
        SellerEntity saved =sellerRepo.save(seller);
        webClient.post()
                .uri("/internal/user/update-role/{userId}",saved.getUserId())
                .retrieve()
                .toBodilessEntity()
                .block();
        return SellerMapper.toDto(seller);


    }

}
