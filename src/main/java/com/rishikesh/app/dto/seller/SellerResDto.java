package com.rishikesh.app.dto.seller;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SellerResDto {

    private String number;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;

}
