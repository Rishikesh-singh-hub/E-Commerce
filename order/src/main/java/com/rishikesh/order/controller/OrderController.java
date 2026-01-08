package com.rishikesh.order.controller;

import com.rishikesh.order.dto.BillResDto;
import com.rishikesh.app.dto.order.OrderReqDto;
import com.rishikesh.app.jwt.JwtUtils;
import com.rishikesh.app.service.OrderService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final JwtUtils jwtUtils;

    public OrderController(OrderService orderService, JwtUtils jwtUtils) {
        this.orderService = orderService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/bill")
    public ResponseEntity<?> generateBill(@RequestBody OrderReqDto orderReq,HttpServletRequest servletRequest){

        logger.info("servletRequest received {}",servletRequest.toString());
        String userId = getUserIdFromRequest(servletRequest);
        BillResDto bill =orderService.generateBill(userId,orderReq);
        return ResponseEntity.ok(bill);

    }

    @PostMapping("/{idempoKey}/{orderId}")
    public ResponseEntity<?> finalOrder(@PathVariable String idempoKey,@PathVariable String orderId,HttpServletRequest servletRequest){

        String userId = getUserIdFromRequest(servletRequest);
        BillResDto bill =orderService.placeOrder(idempoKey,userId,orderId);
            return ResponseEntity.ok(bill);
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