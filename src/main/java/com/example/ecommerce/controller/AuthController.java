package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.AuthResponse;
import com.example.ecommerce.controller.dto.LoginRequest;
import com.example.ecommerce.controller.dto.RefreshTokenRequest;
import com.example.ecommerce.controller.dto.RegisterRequest;
import com.example.ecommerce.services.AuthServiceSecure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthServiceSecure authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Tentative d'inscription pour l'email: {}", request.getEmail());
        try {
            AuthResponse response = authService.register(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            log.warn("Tentative d'enregistrement avec données invalides : {}", e.getMessage());
            
            // Vérifier si c'est une erreur d'email déjà utilisé
            if (e.getMessage().toLowerCase().contains("email")) {
                return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "error", "Email déjà utilisé",
                        "message", "l'adresse email est deja utilisée",
                        "field", "email"
                    ));
            }
            
            // Pour les autres erreurs de validation
            return ResponseEntity.badRequest()
                .body(Map.of(
                    "success", false,
                    "error", "Erreur de validation",
                    "message", e.getMessage(),
                    "field", "general"
                ));
        } catch (RuntimeException e) {
            log.error("Erreur lors de l'enregistrement : {}", e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of(
                        "success", false,
                        "error", "Erreur d'enregistrement",
                        "message", e.getMessage()
                    ));
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", "Erreur serveur",
                        "message", "Une erreur est survenue lors de la création du compte"
                    ));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        log.info("Tentative de connexion pour l'email: {}", request.getEmail());
        try {
            // Log de la tentative de connexion (sans le mot de passe)
            log.info("Tentative de connexion depuis {} pour {}", 
                    httpRequest.getRemoteAddr(), request.getEmail());
            
            AuthResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            log.warn("Tentative de connexion échouée pour {} depuis {}", 
                    request.getEmail(), httpRequest.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                        "success", false,
                        "error", "Authentification échouée",
                        "message", "Identifiants invalides"
                    ));
        } catch (Exception e) {
            log.error("Erreur lors de la connexion : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                        "success", false,
                        "error", "Erreur serveur",
                        "message", "Une erreur est survenue lors de la connexion"
                    ));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        try {
            AuthResponse response = authService.refreshToken(request);
            return ResponseEntity.ok(response);
        } catch (SecurityException e) {
            log.warn("Tentative de refresh avec token invalide : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Token invalide", "message", e.getMessage()));
        } catch (Exception e) {
            log.error("Erreur lors du refresh : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur", "message", "Erreur lors du renouvellement"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            authService.logout(authHeader);
            return ResponseEntity.ok(Map.of("message", "Déconnexion réussie"));
        } catch (Exception e) {
            log.error("Erreur lors de la déconnexion : {}", e.getMessage());
            return ResponseEntity.ok(Map.of("message", "Déconnexion effectuée")); // Toujours retourner OK pour éviter les fuites d'info
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token manquant"));
            }
            
            // Extraire le token
            String token = authHeader.substring(7);
            if (token.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token vide"));
            }
            
            // Valider le token avec le service JWT
            try {
                String username = authService.validateToken(token);
                return ResponseEntity.ok(Map.of(
                    "valid", true, 
                    "message", "Token valide",
                    "username", username
                ));
            } catch (SecurityException e) {
                log.warn("Token invalide : {}", e.getMessage());
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("valid", false, "message", "Token invalide : " + e.getMessage()));
            }
        } catch (Exception e) {
            log.error("Erreur lors de la validation du token : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Erreur de validation"));
        }
    }
}
