package com.altynai.internship.controller;

import com.altynai.internship.dto.UserLoginDto;
import com.altynai.internship.dto.UserRegistrationDto;
import com.altynai.internship.model.User;
import com.altynai.internship.service.JwtService;
import com.altynai.internship.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    // 📌 REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto dto, BindingResult result) {
        // Step 1: Validate fields
        if(result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        // Step 2: Check if email or username already exists
        Optional<User> existingEmail = userService.findByEmail(dto.getEmail());
        if (existingEmail.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }
        Optional<User> existingUsername = userService.findByUsername(dto.getUsername());
        if (existingUsername.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
        }

        // Step 3: Register new user
        userService.registerUser(dto);
        return ResponseEntity.ok("User registered successfully");
    }

    // 📌 Login user (for React)
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginDto dto, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : result.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        User user = userService.findByEmail(dto.getEmail())
                .orElse(null);

        if (user != null && userService.checkPassword(dto.getPassword(), user.getPasswordHash())) {
            String token = jwtService.generateToken(user); // real token

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("message", "Login successful");

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid email or password");
        }
    }
}