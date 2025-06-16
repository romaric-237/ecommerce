package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.UpdateUserDTO;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:5173") // à adapter selon ton front
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO dto) {

        Optional<UserEntity> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur non trouvé.");
        }

        UserEntity user = userOptional.get();
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setMotDePasse(dto.getMotDePasse()); // ⚠️ penser au hash plus tard !

        userRepository.save(user);

        return ResponseEntity.ok("Informations mises à jour avec succès.");
    }
}
