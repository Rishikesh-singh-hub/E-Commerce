package com.rishikesh.cart.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddToCartReqDto {

    @NotBlank(message = "productId is required")
    private String productId;

    @Positive(message = "quantity must be greater than 0")
    private int quantity;
}
