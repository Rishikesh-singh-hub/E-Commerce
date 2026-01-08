package com.rishikesh.order.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
    @Builder.Default
    private List<String> productIds = new ArrayList<>();
    private boolean active;
    private Instant createdAt;

}
