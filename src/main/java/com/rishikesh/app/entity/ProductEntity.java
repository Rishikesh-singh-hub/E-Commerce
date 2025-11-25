package com.rishikesh.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private Double price;
    private Integer stock;   // nullable = unlimited if null
}