package com.salvador.droneControl.infrastructure.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HelloWorldController {
    @GetMapping("/hello")
    public Map<String, Object> sayHello() {
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Hola, mundo!");
        response.put("status", 200);
        return response;
    }
}
