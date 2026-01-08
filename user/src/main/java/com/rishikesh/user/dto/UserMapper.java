package com.rishikesh.user.dto;

import com.rishikesh.user.entity.ROLE;
import com.rishikesh.user.entity.UserEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity toUserEntity(UserDto userDto){

        return UserEntity.builder()
                .email(userDto.getEmail())
                .name(userDto.getName())
                .address(userDto.getAddress())
                .role(List.of(ROLE.CUSTOMER))
                .password(passwordEncoder.encode(userDto.getPassword()))
                .build();

    }

    public UserResponse toUserResponse(UserEntity entity) {

        return UserResponse.builder()
                .username(entity.getName())
                .email(entity.getEmail())
                .address(entity.getAddress())
                .build();

    }
}
