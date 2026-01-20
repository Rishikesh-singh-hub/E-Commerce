package com.rishikesh.product.controller;

import com.rishikesh.product.dto.product.ProductResDto;
import com.rishikesh.product.dto.seller.SellerReqDto;
import com.rishikesh.product.dto.seller.SellerResDto;
import com.rishikesh.product.service.SellerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;
    Logger logger = LoggerFactory.getLogger(SellerController.class);
    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/auth/seller-login")
    public ResponseEntity<?> addSeller(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody SellerReqDto sellerReq){

        SellerResDto resDto = sellerService.addSeller(jwt.getSubject(),sellerReq);

        return ResponseEntity.ok(resDto);

    }

    @GetMapping("/auth/products")
    public ResponseEntity<?> getSellerProduct(@AuthenticationPrincipal Jwt jwt){
        logger.info(jwt.getSubject());
        List<ProductResDto> productResDtoList = sellerService.getProducts(jwt.getSubject());
        if(productResDtoList != null){
            return ResponseEntity.ok(productResDtoList);
        }
        return ResponseEntity.noContent().build();


    }

}
