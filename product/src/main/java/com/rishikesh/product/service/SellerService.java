package com.rishikesh.product.service;

import com.rishikesh.product.dto.product.ProductResDto;
import com.rishikesh.product.dto.seller.SellerMapper;
import com.rishikesh.product.dto.seller.SellerReqDto;
import com.rishikesh.product.dto.seller.SellerResDto;
import com.rishikesh.product.entity.ProductEntity;
import com.rishikesh.product.entity.SellerEntity;
import com.rishikesh.product.mapper.ProductMapper;
import com.rishikesh.product.repository.ProductRepo;
import com.rishikesh.product.repository.SellerCriteria;
import com.rishikesh.product.repository.SellerRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.swing.*;
import java.util.List;

@Slf4j
@Service
@Lazy
public class SellerService {

    Logger logger = LoggerFactory.getLogger(SellerService.class);
    private final SellerRepo sellerRepo;
    private final ProductRepo productRepo;
    private final WebClient webClient;
    @Value("${internal.secret}")
    private String INTERNAL_TOKEN;

    public SellerService(SellerRepo sellerRepo, ProductRepo productRepo, WebClient.Builder webClient) {
        this.sellerRepo = sellerRepo;
        this.productRepo = productRepo;
        this.webClient = webClient
                .baseUrl("http://localhost:8080")
                .build();
    }

    public SellerResDto addSeller(String userId, SellerReqDto sellerReqDto){
        logger.info("seller service token {}", INTERNAL_TOKEN);
        SellerEntity seller = SellerMapper.toEntity(sellerReqDto,userId);
        SellerEntity saved =sellerRepo.save(seller);
        webClient.post()
                .uri("/internal/user/update-role/{userId}",saved.getUserId())
                .header("X-internal-secret",INTERNAL_TOKEN)
                .retrieve()
                .toBodilessEntity()
                .block();
        return SellerMapper.toDto(seller);


    }

    public List<ProductResDto> getProducts(String userId) {
        String sellerId = SellerCriteria.findSellerIdByUserId(userId);
        List<ProductEntity> productEntities = productRepo.findAllBySellerId(sellerId);
        List<ProductResDto> productResDtos = productEntities
                .stream()
                .map(ProductMapper::toResDto)
                .toList();
        return productResDtos;
    }
}
