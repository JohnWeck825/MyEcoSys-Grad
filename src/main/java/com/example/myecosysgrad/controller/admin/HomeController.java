package com.example.myecosysgrad.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class HomeController {
    @GetMapping
    public String home() {
        return "Hello Admin";
    }
}
