package com.rishikesh.user.entity;

import com.rishikesh.contracts.dto.seller.SellerStatus;
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
    private String shopName;
    private String shopAddress;
    private String gstNumber;
    @Indexed(unique = true)
    private String phoneNumber;
    private SellerStatus sellerStatus;
    private Instant createdAt;

}
