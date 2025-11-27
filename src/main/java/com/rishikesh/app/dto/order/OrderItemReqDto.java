package com.rishikesh.app.dto.order;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemReqDto {
    private String productId;
    private int quantity;
}
