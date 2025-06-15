package com.example.ecommerce.controller;

import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserEntity user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email déjà utilisé.";
        }

        userRepository.save(user);
        return "Utilisateur enregistré avec succès.";
    }
}