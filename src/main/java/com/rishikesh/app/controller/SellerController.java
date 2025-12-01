package com.rishikesh.app.controller;

import com.rishikesh.app.dto.seller.SellerReqDto;
import com.rishikesh.app.dto.seller.SellerResDto;
import com.rishikesh.app.jwt.JwtUtils;
import com.rishikesh.app.service.SellerService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/seller")
public class SellerController {

    private final JwtUtils jwtUtils;
    private final SellerService sellerService;

    public SellerController(JwtUtils jwtUtils, SellerService sellerService) {
        this.jwtUtils = jwtUtils;
        this.sellerService = sellerService;
    }

    @PostMapping
    public ResponseEntity<?> sellerSigning(@Valid @RequestBody SellerReqDto sellerReq, HttpServletRequest req){
        String userId = getUserIdFromRequest(req);
        SellerResDto sellerRes= sellerService.signin(sellerReq,userId);
        return ResponseEntity.ok(sellerRes);
    }

    private String getUserIdFromRequest(HttpServletRequest request){

        String jwt = request.getHeader("Authorization").substring(7);
        Claims claims = jwtUtils.parseClaims(jwt);

        String userId = claims.getSubject();
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("missing subject");
        }
        return userId;
    }

}
