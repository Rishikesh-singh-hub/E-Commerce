package com.rishikesh.app.dto.bill;

import com.rishikesh.app.dto.product.ProductDto;
import com.rishikesh.app.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResDto {

    private String id;
    private String orderId;
    private String userId;
    private List<ProductDto> productDtoList;
    private BigDecimal amount;
    private Status status;
    private Instant issuedAt;
    private Instant updatedAt;

}
