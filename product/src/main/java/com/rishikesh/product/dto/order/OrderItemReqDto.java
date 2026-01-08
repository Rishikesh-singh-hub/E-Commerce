package com.rishikesh.product.dto.order;

import lombok.Data;

@Data
public class OrderItemReqDto {
    private String productId;
    private int quantity;
}
