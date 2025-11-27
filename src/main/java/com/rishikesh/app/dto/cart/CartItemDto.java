package com.rishikesh.app.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class CartItemDto {
    private String ProductId;
    private String productName;
    private BigDecimal price;
    private int quantity;
}
