package com.rishikesh.contracts.dto.seller;

import lombok.Builder;
import lombok.Data;

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
