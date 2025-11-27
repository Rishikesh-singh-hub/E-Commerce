package com.rishikesh.app.dto.order;

import lombok.Data;
import java.util.List;

@Data
public class OrderReqDto {
    private List<OrderItemReqDto> items;
}
