package EmployeeManagement.controller;

import EmployeeManagement.entity.User;
import EmployeeManagement.exception.AppException;
import EmployeeManagement.repository.UserRepository;
import EmployeeManagement.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenController {

    @Autowired private JwtUtil             jwtUtil;
    @Autowired private UserRepository      userRepository;
    @Autowired private BCryptPasswordEncoder encoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            throw AppException.invalidInput("Username and password are required.");
        }
        if (userRepository.existsByUsername(username.trim())) {
            throw AppException.emailDuplicate(username.trim());  // reusing conflict factory
        }

        User user = new User(username.trim(), encoder.encode(password), "USER");
        userRepository.save(user);
        String token = jwtUtil.generateToken(username.trim());
        return ResponseEntity.ok(Map.of("token", token, "username", username.trim()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        if (username == null || password == null) {
            throw AppException.invalidInput("Username and password are required.");
        }

        User user = userRepository.findByUsername(username.trim()).orElse(null);
        if (user == null || !encoder.matches(password, user.getPassword())) {
            throw AppException.unauthorized("Invalid username or password.");
        }

        String token = jwtUtil.generateToken(username.trim());
        return ResponseEntity.ok(Map.of("token", token, "username", username.trim()));
    }
}