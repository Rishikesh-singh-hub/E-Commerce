package com.rishikesh.app.dto.seller;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerReqDto {

    @NotBlank(message = "Shop name is required")
    @Size(max = 100, message = "Shop name cannot exceed 100 characters")
    private String shopName;

    @NotBlank(message = "Shop address is required")
    @Size(max = 255, message = "Shop address cannot exceed 255 characters")
    private String shopAddress;

    @Pattern(
            regexp = "^[0-9A-Z]{8,15}$",
            message = "Invalid GST number format"
    )
    private String gstNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Phone number must be exactly 10 digits"
    )
    private String phoneNumber;

}
