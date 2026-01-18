package com.rishikesh.user.controller;

import com.rishikesh.user.service.InternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/user")
public class InternalController {
    private final InternalService internalService;
    Logger logger = LoggerFactory.getLogger(InternalController.class);
    public InternalController(InternalService internalService) {
        this.internalService = internalService;
    }

    @PostMapping("/update-role/{userId}")
    public void updateRole(@PathVariable String userId){

        internalService.updateRole(userId);
    }

}
