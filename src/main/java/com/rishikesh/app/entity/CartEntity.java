package com.rishikesh.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {
    @Id
    private String id;
    private String userId;                // store user id as string
    @Builder.Default
    private List<CartItemEntity> items = new ArrayList<>();
    private Status status;
    private Instant updatedAt;
}
