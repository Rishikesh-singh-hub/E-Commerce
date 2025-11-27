package com.rishikesh.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "orders")
@CompoundIndex(def = "{'userId': 1, 'idempotencyKey': 1}", name = "userId_idempotencyKey_idx", unique = true, sparse = true)
public class OrderEntity {
    @Id
    private String id;

    /**
     * User identifier (from JWT sub or claim). Must be present.
     */
    private String userId;

    /**
     * Optional idempotency key (use when you later add idempotency).
     * Kept nullable for now — index is sparse so it won't enforce uniqueness for nulls.
     */
    private String idempotencyKey;

    /**
     * Snapshot of cart items at time of order creation.
     */
    private List<OrderItem> items;

    /**
     * Total snapshot (sum of priceAtPurchase * qty).
     */
    private BigDecimal total;

    /**
     * CREATED, PAYMENT_PENDING, PAID, CANCELLED, etc.
     */
    private Status status;

    @Builder.Default
    private Instant createdAt = Instant.now();

    @Builder.Default
    private Instant updatedAt = Instant.now();
}