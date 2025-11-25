package com.rishikesh.app.mapper;

import com.rishikesh.app.dto.cart.CartItemDto;
import com.rishikesh.app.dto.cart.CartResDto;
import com.rishikesh.app.entity.CartEntity;
import com.rishikesh.app.entity.CartItemEntity;
import com.rishikesh.app.repository.CartRepo;

import java.util.stream.Collectors;

public class CartMapper {

    private static  CartRepo cartRepo;

    public CartMapper(CartRepo cartRepo) {
        CartMapper.cartRepo = cartRepo;
    }

    public static CartResDto toResponse(CartEntity cart) {
        if (cart == null) return null;

        return CartResDto.builder()
                .id(cart.getId())
                .items(
                        cart.getItems()
                                .stream()
                                .map(CartMapper::toItemDto)
                                .collect(Collectors.toList())
                )
                .build();
    }

    private static CartItemDto toItemDto(CartItemEntity item) {
        return CartItemDto.builder()
                .ProductId(item.getProductId())
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .build();
    }

    public static CartEntity toEntity(CartResDto cartResDto,String userId) {

        return CartEntity.builder()
                .id(cartResDto.getId())
                .userId(userId)
                .items(
                        cartResDto.getItems()
                                .stream()
                                .map(CartMapper::toItemEntity)
                                .collect(Collectors.toList())
                )
                .build();

    }

    private static CartItemEntity toItemEntity(CartItemDto cartItemDto) {

        return CartItemEntity.builder()
                .productId(cartItemDto.getProductId())
                .productName(cartItemDto.getProductName())
                .price(cartItemDto.getPrice())
                .quantity(cartItemDto.getQuantity())
                .build();


    }
}
