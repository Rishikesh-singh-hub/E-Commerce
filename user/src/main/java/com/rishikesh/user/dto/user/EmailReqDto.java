package com.rishikesh.user.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailReqDto {
    @NotBlank
   private String email;
    private String otp;
}
