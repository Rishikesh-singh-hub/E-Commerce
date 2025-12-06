package com.rishikesh.user.dto.user;

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
