package com.rishikesh.app.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UserResponse {

    private String username;
    private String email;
    private String address;


}
