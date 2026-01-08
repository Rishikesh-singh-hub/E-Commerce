package com.rishikesh.order.dto;

import lombok.Data;

@Data
public class OrderItemReqDto {
    private String productId;
    private int quantity;
}
