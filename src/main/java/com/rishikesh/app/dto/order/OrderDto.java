package com.rishikesh.app.dto.order;

import com.rishikesh.app.entity.Status;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
public class OrderDto {
    private String id;
    private String userId;
    private String idempotencyKey;
    private List<OrderItemDto> items;
    private BigDecimal total;
    private Status status;
    private Instant createdAt;
    private Instant updatedAt;


}