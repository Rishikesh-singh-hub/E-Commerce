package com.rishikesh.app.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

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
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location; // GeoJSON Point: coordinates [lng, lat]
    private boolean active;
    private Instant createdAt;

}
