package EmployeeManagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import EmployeeManagement.security.JwtUtil;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenController {

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestParam String username) {
        String token = jwtUtil.generateToken(username);
        return Map.of("token", token);
    }
}