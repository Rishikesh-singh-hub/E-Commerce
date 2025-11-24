package com.rishikesh.app.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    private String name;
    private String email;
    private String address;
    private String jwt;

}
