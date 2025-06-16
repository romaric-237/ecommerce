package com.example.ecommerce.controller;

import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.controller.dto.LoginResponse;
import com.example.ecommerce.controller.dto.LoginResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserEntity user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return "Email déjà utilisé.";
        }

        userRepository.save(user);
        return "Utilisateur enregistré avec succès.";
    }

    @PostMapping("/login")
public ResponseEntity<?> loginUser(@RequestBody UserEntity loginData) {
    Optional<UserEntity> userOpt = userRepository.findByEmail(loginData.getEmail());

    if (userOpt.isEmpty()) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email incorrect.");
    }

    UserEntity user = userOpt.get();

    if (!user.getMotDePasse().equals(loginData.getMotDePasse())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
    }

    String token = jwtUtil.generateToken(user.getEmail());  // ✅ Génère le token

    return ResponseEntity.ok(new LoginResponse(token, "Connexion réussie."));
}

}
