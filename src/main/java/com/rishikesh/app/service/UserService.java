package com.rishikesh.user.service;

import com.rishikesh.user.dto.*;
import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.jwt.JwtUtils;
import com.rishikesh.user.repository.UserRepo;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Component
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserRepo userRepo;

    public UserService(AuthenticationManager authManager, JwtUtils jwtUtils, UserMapper userMapper, UserRepo userRepo) {
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userRepo = userRepo;
    }

    public UserLoginDto userLogin(UserRequest request){
        try{

            Authentication auth = authManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(auth);
            UserEntity entity = userRepo.findByEmail(request.getEmail()).orElseThrow();
            String jwt = jwtUtils.generateJwt(entity);
            return UserLoginDto.builder()
                    .name(entity.getName())
                    .email(entity.getEmail())
                    .address(entity.getAddress())
                    .jwt(jwt)
                    .build();


        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return null;
    }

    public UserResponse signupUser(@Valid UserDto userDto) {

        UserEntity entity = userMapper.toUserEntity(userDto);
        userRepo.save(entity);
        return  userMapper.toUserResponse(entity);

    }
}
