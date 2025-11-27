package com.rishikesh.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "products")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
    @Id

    private String id;        // Mongo ObjectId as hex string

    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;   // nullable = unlimited if null
}