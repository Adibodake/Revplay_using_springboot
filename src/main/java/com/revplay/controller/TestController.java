package com.revplay.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/me")
    public String me() {
        return "JWT working! You are authenticated.";
    }
}
