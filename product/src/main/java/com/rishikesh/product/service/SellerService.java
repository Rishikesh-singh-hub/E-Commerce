package com.rishikesh.product.service;

import com.rishikesh.product.dto.product.ProductResDto;
import com.rishikesh.product.entity.ProductEntity;
import com.rishikesh.product.mapper.ProductMapper;
import com.rishikesh.product.repository.ProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Slf4j
@Service
@Lazy
public class SellerService {

    Logger logger = LoggerFactory.getLogger(SellerService.class);
    private final ProductRepo productRepo;

    public SellerService(ProductRepo productRepo,
                         WebClient.Builder webClient) {
        this.productRepo = productRepo;
    }



    public List<ProductResDto> getProducts(String userId) {
        List<ProductEntity> productEntities = productRepo.findAllBySellerId(userId);
        return productEntities
                .stream()
                .map(ProductMapper::toResDto)
                .toList();
    }
}
