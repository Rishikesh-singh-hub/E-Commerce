package com.rishikesh.app.mapper;

import com.rishikesh.app.dto.bill.BillResDto;
import com.rishikesh.app.dto.order.OrderItemDto;
import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.entity.BillEntity;
import com.rishikesh.app.entity.OrderItem;
import com.rishikesh.app.entity.ProductEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BillMapper {

    public static BillResDto toDto(BillEntity entity){

        return BillResDto.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .userId(entity.getUserId())
                .productDtoList(toProductDtoList(entity.getProductsList()))
                .amount(entity.getAmount())
                .status(entity.getStatus())
                .issuedAt(entity.getIssuedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();

    }

    public static List<ProductDto> toProductDtoList(List<ProductEntity> entities) {
        if (entities == null) return Collections.emptyList();
        return entities.stream()
                .filter(Objects::nonNull)
                .map(BillMapper::toProductDto)
                .collect(Collectors.toList());
    }

    public static ProductDto toProductDto(ProductEntity entity) {
        if (entity == null) return null;
        return ProductDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }

}
