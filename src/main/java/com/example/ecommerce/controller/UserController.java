package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.UserRegistrationDTO;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Inscription réussie pour " + user.getPrenom(),
                "user", user
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "error", "Erreur de validation",
                    "message", e.getMessage()
                ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "error", "Erreur d'enregistrement",
                    "message", e.getMessage()
                ));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRegistrationDTO dto) {
        try {
            UserEntity updatedUser = userService.updateUser(id, dto);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "Profil mis à jour avec succès",
                "user", updatedUser
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "error", "Erreur de validation",
                    "message", e.getMessage()
                ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "error", "Erreur serveur",
                    "message", "Une erreur est survenue lors de la mise à jour du profil"
                ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            UserEntity user = userService.findById(id);
            return ResponseEntity.ok(Map.of(
                "success", true,
                "user", user
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "error", "Utilisateur non trouvé",
                    "message", e.getMessage()
                ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "success", false,
                    "error", "Erreur serveur",
                    "message", "Une erreur est survenue lors de la récupération du profil"
                ));
        }
    }
}
