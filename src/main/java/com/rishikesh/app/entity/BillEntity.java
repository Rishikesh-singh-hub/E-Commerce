package com.rishikesh.app.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "bills")
public class BillEntity {
    private String id;
    private String orderId;
    private List<ProductEntity> productsList;
    private String userId;
    private BigDecimal amount;
    private String paymentId;
    private String idempotencyKey;
    private String paymentIntent;
    private Status status;
    @Builder.Default
    private Instant issuedAt = Instant.now();
    private Instant updatedAt;
}
