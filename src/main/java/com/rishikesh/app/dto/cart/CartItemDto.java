package com.rishikesh.app.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public  class CartItemDto {
    private String ProductId;
    private String productName;
    private Double price;
    private int quantity;
}
