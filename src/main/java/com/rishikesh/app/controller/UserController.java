package com.rishikesh.app.controller;

import com.rishikesh.app.dto.UserDto;
import com.rishikesh.app.dto.UserLoginDto;
import com.rishikesh.app.dto.UserRequest;
import com.rishikesh.app.dto.UserResponse;
import com.rishikesh.app.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
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

        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/signing")
    public ResponseEntity<?> signing(@Valid @RequestBody UserRequest request){

        UserLoginDto loginDto = userService.userLogin(request);
        if (loginDto!=null)
                return ResponseEntity.ok(loginDto);
        return ResponseEntity.noContent().build();
    }

}
