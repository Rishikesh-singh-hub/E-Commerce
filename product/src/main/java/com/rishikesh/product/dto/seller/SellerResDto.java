package com.rishikesh.product.dto.seller;

import com.rishikesh.product.entity.SellerStatus;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
public class SellerResDto {

    private String sellerId;

    private String shopName;
    private String shopAddress;
    private String gstNumber;
    private String phoneNumber;

    private SellerStatus status;

    private Instant createdAt;
}
