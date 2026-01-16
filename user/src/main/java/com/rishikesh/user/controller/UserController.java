package com.rishikesh.user.controller;

import com.rishikesh.user.dto.*;
import com.rishikesh.user.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
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
