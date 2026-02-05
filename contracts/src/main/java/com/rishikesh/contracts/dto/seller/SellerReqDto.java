package com.rishikesh.contracts.dto.seller;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Data
@Setter
@Getter
public class SellerReqDto {

    @NotBlank(message = "Shop name is required")
    private String shopName;

    @NotBlank(message = "Shop address is required")
    private String shopAddress;

    @NotBlank(message = "GST number is required")
    private String gstNumber;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
}
