package org.example.controller;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/greeting")
public class GreetingController {

    @GetMapping
    public Map<String, String> greet(@RequestParam(defaultValue = "World") String name) {
        return Map.of("message", "Hello, " + name + "!");
    }

    @PostMapping
    public Map<String, String> echo(@RequestBody Map<String, String> body) {
        return Map.of("received", body.getOrDefault("content", ""));
    }
}
