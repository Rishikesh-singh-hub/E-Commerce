package com.rishikesh.product.entity;

import com.rishikesh.product.service.SellerService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sellers")
public class SellerEntity {

    @Id
    private String id;
    @Indexed(unique = true)
    private String userId;
    private String shopName;
    private String shopAddress;
    private String gstNumber;
    @Indexed(unique = true)
    private String phoneNumber;
    private SellerStatus sellerStatus;
    private Instant createdAt;

}
