package com.rishikesh.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cartItem")
public class CartItemEntity {
    @Id
    private String id; // optional client-generated id or null
    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
}
