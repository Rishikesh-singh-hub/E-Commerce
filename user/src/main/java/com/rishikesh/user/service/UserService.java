package com.rishikesh.user.service;

import com.nimbusds.jose.JOSEException;
import com.rishikesh.contracts.exception.EmailAlreadyExistException;
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

import java.security.SecureRandom;


@Component
public class UserService {

    Logger logger = LoggerFactory.getLogger(UserService.class);

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AuthenticationManager authManager;
    private final JwtUtils jwtUtils;
    private final UserMapper userMapper;
    private final UserRepo userRepo;
    private final Services service;

    public UserService(AuthenticationManager authManager, JwtUtils jwtUtils, UserMapper userMapper, UserRepo userRepo, Services service) {
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.userRepo = userRepo;
        this.service = service;
    }

    public UserLoginDto userLogin(UserRequest request){

            UserEntity checkEntity = userRepo.findByEmail(request.getEmail()).orElse(null);
            if (  checkEntity != null && checkEntity.isActive()){
                logger.info("user found");
                Authentication auth = authManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(auth);
                UserEntity entity = userRepo.findByEmail(request.getEmail()).orElseThrow();
                String jwt = null;
                try {
                    jwt = jwtUtils.generateJwt(entity);
                } catch (JOSEException e) {
                    throw new RuntimeException(e);
                }
                return UserLoginDto.builder()
                        .name(entity.getName())
                        .email(entity.getEmail())
                        .address(entity.getAddress())
                        .jwt(jwt)
                        .build();
            }
            logger.info("user not found");
            return null;

    }

    public UserResponse signupUser(@Valid UserDto userDto) {

        UserEntity entity = userMapper.toUserEntity(userDto);
        if(userRepo.existsByEmail(entity.getEmail())){
            throw new EmailAlreadyExistException("Email Already exist");
        }
        entity.setActive(false);
        userRepo.save(entity);
        return  userMapper.toUserResponse(entity);


    }
    public boolean sendOtp(EmailReqDto email) {

        UserEntity entity =  userRepo.findByEmail(email.getEmail()).orElse(null);
        if(entity.isActive()){
            return false;
        }

        String otp = generate6Digit();
        service.storeOtp(email.getEmail(),otp);
        String body =  String.format(
                "Hello,\n\nYour one-time security code is: %s\n\n"
                        + "It will expire in 10 minutes. Do not share this code with anyone.\n"
                        + "If you did not initiate this request, simply ignore this email.\n\n"
                        + "Best regards,\nThe Team",
                otp
        );
        logger.info("email {}",email.getEmail());
        service.sendEmail(email.getEmail(),"Security Code for Your Account",body);
        return true;

    }



    private static String generateNumericOtp(int length) {
        if (length <= 0) throw new IllegalArgumentException("length must be > 0");
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(RANDOM.nextInt(10));
        }
        return sb.toString();
    }

    private static String generate6Digit() {
        return generateNumericOtp(6);
    }

    public boolean verifyOtp(EmailReqDto email) {
        logger.info("verifying email");



        boolean verified = service.checkOtp(email);
        logger.info("Verified ? {}",verified);
        if (verified){
            UserEntity entity = userRepo.findByEmail(email.getEmail()).orElse(null);
            entity.setActive(true);
            userRepo.save(entity);

            return true;
        }

        return false;

    }
}
