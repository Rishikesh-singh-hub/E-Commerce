package com.rishikesh.app.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "idempotency")
@Builder
public class IdempotencyRecord {
    @Id
    private String id; // composite key e.g. userId:idemKey
    private String userId;
    private String idempotencyKey;
    private String orderId; // once created
    private Status status; // IN_PROGRESS, COMPLETED, FAILED
    private Instant createdAt;
    private Instant updatedAt;
//    private String responseSnapshot; // optional JSON of response
}
