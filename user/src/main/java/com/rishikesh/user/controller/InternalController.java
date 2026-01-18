package com.rishikesh.user.controller;

import com.rishikesh.user.service.InternalService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/user")
public class InternalController {
    private final InternalService internalService;

    public InternalController(InternalService internalService) {
        this.internalService = internalService;
    }

    @PostMapping("/update-role/{userId}")
    public void updateRole(String userId){

        internalService.updateRole(userId);
    }

}
