package com.rishikesh.app.dto.cart;


import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResDto {

    private String id;
    private List<CartItemDto> items;


}