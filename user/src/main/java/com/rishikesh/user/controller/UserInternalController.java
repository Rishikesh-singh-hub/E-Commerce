package com.rishikesh.user.controller;

import com.rishikesh.user.entity.UserEntity;
import com.rishikesh.user.service.InternalService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/internal")
public class UserInternalController {

    private final InternalService internalService;

    public UserInternalController(InternalService internalService) {
        this.internalService = internalService;
    }

    @GetMapping("/{id}")
    public UserEntity getById(@PathVariable String id){

        return internalService.getById(id);

    }

}
