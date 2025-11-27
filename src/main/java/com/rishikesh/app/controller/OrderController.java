package com.rishikesh.app.controller;

import com.rishikesh.app.dto.bill.BillResDto;
import com.rishikesh.app.dto.order.OrderDto;
import com.rishikesh.app.dto.order.OrderReqDto;
import com.rishikesh.app.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<?> generateBill(@PathVariable String userId, @RequestBody OrderReqDto orderReq){

        BillResDto bill =orderService.generateBill(userId,orderReq);
        return ResponseEntity.ok(bill);

    }

    @PostMapping("/{idempoKey}/{orderId}/{userId}")
    public ResponseEntity<?> finalOrder(@PathVariable String idempoKey,@PathVariable String orderId,@PathVariable String userId){
            BillResDto bill =orderService.placeOrder(idempoKey,userId,orderId);
            return ResponseEntity.ok(bill);
    }

}