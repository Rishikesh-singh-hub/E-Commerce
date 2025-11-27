package com.rishikesh.app.dto.order;

import com.rishikesh.app.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderTotalResult {
    private List<ProductEntity> updatedProducts;
    private BigDecimal total;
}