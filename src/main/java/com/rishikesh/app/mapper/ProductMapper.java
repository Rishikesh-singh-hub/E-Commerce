package com.rishikesh.app.mapper;

import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.dto.product.ProductResDto;
import com.rishikesh.app.entity.ProductEntity;

public class ProductMapper {

    public static ProductDto toDto (ProductEntity entity){

        return ProductDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .build();
    }

    public static ProductResDto toResDto (ProductEntity entity){

        return ProductResDto.builder()
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .stock(entity.getStock())
                .secureUrl(entity.getSecureUrl())
                .build();
    }

    public static ProductEntity toEntity(ProductDto dto) {
        if (dto == null) return null;

        return ProductEntity.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock())
                .build();
    }

}
