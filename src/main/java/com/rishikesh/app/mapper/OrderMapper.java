package com.rishikesh.app.mapper;

import com.rishikesh.app.dto.order.OrderDto;
import com.rishikesh.app.dto.order.OrderItemDto;
import com.rishikesh.app.dto.order.OrderReqDto;
import com.rishikesh.app.entity.OrderEntity;
import com.rishikesh.app.entity.OrderItem;
import com.rishikesh.app.entity.ProductEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class OrderMapper {



    /* -------------------- OrderItem mappings -------------------- */

    public static OrderItemDto toItemDto(OrderItem item) {
        if (item == null) return null;
        return OrderItemDto.builder()
                        .productId(item.getProductId())
                        .qty(item.getQty())
                        .priceAtPurchase(item.getPriceAtPurchase())
                        .title(item.getTitle())
                        .build();
    }

    public static OrderItem toItemEntity(OrderItemDto dto) {
        if (dto == null) return null;
        OrderItem item = new OrderItem();
        item.setProductId(dto.getProductId());
        item.setQty(dto.getQty());
        item.setPriceAtPurchase(dto.getPriceAtPurchase());
        item.setTitle(dto.getTitle());
        return item;
    }

    public static List<OrderItemDto> toItemDtoList(List<OrderItem> items) {
        if (items == null) return Collections.emptyList();
        return items.stream()
                .filter(Objects::nonNull)
                .map(OrderMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public static List<OrderItem> toItemEntityList(List<OrderItemDto> dtos) {
        if (dtos == null) return Collections.emptyList();
        return dtos.stream()
                .filter(Objects::nonNull)
                .map(OrderMapper::toItemEntity)
                .collect(Collectors.toList());
    }

    /* -------------------- Order mappings -------------------- */

    public static OrderDto toOrderDto(OrderEntity entity) {
        if (entity == null) return null;

        return OrderDto.builder()
                        .id(entity.getId())
                        .userId(entity.getUserId())
                        .idempotencyKey(entity.getIdempotencyKey())
                        .items(toItemDtoList(entity.getItems()))
                        .total(entity.getTotal())
                        .status(entity.getStatus())
                        .createdAt(entity.getCreatedAt())
                        .updatedAt(entity.getUpdatedAt())
                        .build();

    }

    public static OrderEntity toOrderEntity(OrderDto dto) {
        if (dto == null) return null;

        OrderEntity entity = new OrderEntity();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setIdempotencyKey(dto.getIdempotencyKey());
        entity.setItems(toItemEntityList(dto.getItems()));
        entity.setTotal(dto.getTotal());

        if (dto.getStatus() != null) {
            try {
                entity.setStatus(dto.getStatus());
            } catch (IllegalArgumentException ex) {
                // unknown status string -> keep default or null
                entity.setStatus(null);
            }
        } else {
            entity.setStatus(null);
        }

        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        return entity;
    }


    public static OrderItem toOrderItem(ProductEntity item) {

        return OrderItem.builder()
                .productId(item.getId())
                .title(item.getName())
                .qty(item.getStock())
                .priceAtPurchase(item.getPrice())
                .build();

    }
}












