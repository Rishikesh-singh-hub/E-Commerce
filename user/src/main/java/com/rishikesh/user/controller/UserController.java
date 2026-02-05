package com.rishikesh.user.controller;

import com.rishikesh.contracts.dto.seller.SellerReqDto;
import com.rishikesh.contracts.dto.seller.SellerResDto;
import com.rishikesh.user.dto.*;
import com.rishikesh.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;


    Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/auth")
    public String auth(){
        return "ok";
    }

    @PostMapping("/auth/seller-signin")
    public ResponseEntity<?> addSeller(@AuthenticationPrincipal Jwt jwt, @Valid @RequestBody SellerReqDto sellerReq){

        SellerResDto resDto = userService.addSeller(jwt.getSubject(),sellerReq);
        if(resDto == null){
            ResponseEntity.badRequest().body("user already exist");
        }

        return ResponseEntity.ok(resDto);

    }

    @GetMapping
    public ResponseEntity<?> healthCheck(){
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<?> singup (@Valid @RequestBody UserDto userDto){
        UserResponse userResponse = userService.signupUser(userDto);

        return ResponseEntity.created(URI.create("/")).build();
    }

    @PostMapping("/signing")
    public ResponseEntity<?> signing(@Valid @RequestBody UserRequest request){

        logger.info("got signin request");

        UserLoginDto loginDto = userService.userLogin(request);
        if (loginDto!=null)
                return ResponseEntity.ok(loginDto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verify(@RequestBody EmailReqDto email){
        logger.info("verify req received");
        boolean verification = userService.sendOtp(email);
        if (verification){
            return ResponseEntity.ok("Otp send");
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody EmailReqDto email){
        boolean verified = userService.verifyOtp(email);
        if (verified){
            return ResponseEntity.ok("Id created");
        }
        return ResponseEntity.badRequest().build();

    }
}
