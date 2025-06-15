package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.UserRegistrationDTO;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        try {
            UserEntity user = userService.registerUser(registrationDTO);
            return ResponseEntity.ok("Inscription réussie pour " + user.getPrenom());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
} 