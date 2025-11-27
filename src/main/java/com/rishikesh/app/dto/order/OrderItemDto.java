package com.rishikesh.app.dto.order;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderItemDto {
    private String productId;
    private int qty;
    private BigDecimal priceAtPurchase;
    private String title;


}
