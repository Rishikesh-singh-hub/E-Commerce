package com.rishikesh.app.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {
    private String productId;
    private String title;
    private int qty;
    private BigDecimal priceAtPurchase;
//    private String sku; // optional, include if you snapshot SKU
}