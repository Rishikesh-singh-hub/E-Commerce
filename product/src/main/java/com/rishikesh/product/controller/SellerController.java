package com.rishikesh.product.controller;

import com.rishikesh.product.dto.seller.SellerReqDto;
import com.rishikesh.product.dto.seller.SellerResDto;
import com.rishikesh.product.service.SellerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final SellerService sellerService;

    public SellerController(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @PostMapping("/add-seller")
    public ResponseEntity<?> addSeller(@AuthenticationPrincipal Jwt jwt, @Valid SellerReqDto sellerReq){

        SellerResDto resDto = sellerService.addSeller(jwt.getSubject(),sellerReq);

        return ResponseEntity.ok(resDto);

    }

}
