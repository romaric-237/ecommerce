package com.example.ecommerce.services;

import com.example.ecommerce.controller.dto.AuthResponse;
import com.example.ecommerce.controller.dto.LoginRequest;
import com.example.ecommerce.controller.dto.RefreshTokenRequest;
import com.example.ecommerce.controller.dto.RegisterRequest;
import com.example.ecommerce.controller.dto.UserDTO;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.UserRepository;
import com.example.ecommerce.security.JwtService;
import com.example.ecommerce.security.UserPrincipal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
public class AuthServiceSecure {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceSecure.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceSecure(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Value("${jwt.access-token-expiration:900000}")
    private long accessTokenExpiration;

    // Pattern pour validation d'email
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
    );

    // Pattern pour validation de mot de passe (au moins 8 caractères, 1 majuscule, 1 minuscule, 1 chiffre)
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,}$"
    );

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            // Validation des données d'entrée
            validateRegistrationRequest(request);

            // Vérifier si l'email existe déjà
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Un compte avec cette adresse email existe déjà");
            }

            var user = new UserEntity();
            user.setNom(sanitizeInput(request.getNom()));
            user.setPrenom(sanitizeInput(request.getPrenom()));
            user.setEmail(request.getEmail().toLowerCase().trim());
            user.setAdresse(sanitizeInput(request.getAdresse()));
            user.setCodePostal(sanitizeInput(request.getCodePostal()));
            user.setVille(sanitizeInput(request.getVille()));
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            userRepository.save(user);
            log.info("Nouvel utilisateur enregistré : {}", user.getEmail());

            return generateAuthResponse(user);
        } catch (Exception e) {
            log.error("Erreur lors de l'enregistrement : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la création du compte : " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        try {
            // Validation des données d'entrée
            validateLoginRequest(request);

            // Tentative d'authentification
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail().toLowerCase().trim(),
                            request.getPassword()
                    )
            );

            UserEntity user = userRepository.findByEmail(request.getEmail().toLowerCase().trim())
                    .orElseThrow(() -> new BadCredentialsException("Identifiants invalides"));

            log.info("Connexion réussie pour l'utilisateur : {}", user.getEmail());
            return generateAuthResponse(user);

        } catch (AuthenticationException e) {
            log.warn("Tentative de connexion échouée pour : {}", request.getEmail());
            throw new BadCredentialsException("Identifiants invalides");
        } catch (Exception e) {
            log.error("Erreur lors de la connexion : {}", e.getMessage());
            throw new RuntimeException("Erreur lors de la connexion");
        }
    }

    @Transactional(readOnly = true)
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();

            // Vérifier que c'est bien un refresh token
            if (!jwtService.isRefreshToken(refreshToken)) {
                throw new SecurityException("Token de type invalide");
            }

            // Extraire le nom d'utilisateur du refresh token
            String userEmail = jwtService.extractUsername(refreshToken);

            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow(() -> new BadCredentialsException("Utilisateur non trouvé"));

            // Créer UserPrincipal pour la validation
            UserPrincipal userPrincipal = UserPrincipal.create(user);

            // Vérifier la validité du refresh token
            if (!jwtService.isTokenValid(refreshToken, userPrincipal)) {
                throw new SecurityException("Refresh token invalide ou expiré");
            }

            // Révoquer l'ancien refresh token
            jwtService.revokeToken(refreshToken);

            log.info("Refresh token utilisé avec succès pour : {}", userEmail);
            return generateAuthResponse(user);

        } catch (Exception e) {
            log.error("Erreur lors du refresh du token : {}", e.getMessage());
            throw new SecurityException("Erreur lors du renouvellement du token : " + e.getMessage());
        }
    }

    @Transactional
    public void logout(String accessToken) {
        try {
            if (accessToken != null && accessToken.startsWith("Bearer ")) {
                String token = accessToken.substring(7);
                jwtService.revokeToken(token);

                String userEmail = jwtService.extractUsername(token);
                log.info("Déconnexion réussie pour : {}", userEmail);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la déconnexion : {}", e.getMessage());
        }
    }

    private AuthResponse generateAuthResponse(UserEntity user) {
        UserPrincipal userPrincipal = UserPrincipal.create(user);
        String accessToken = jwtService.generateAccessToken(userPrincipal);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);


        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(accessToken);
        authResponse.setRefreshToken(refreshToken);
        authResponse.setUser(mapToUserDTO(user));
        authResponse.setTokenType("Bearer");
        authResponse.setExpiresIn(accessTokenExpiration / 1000); // Convertir ms en secondes

        return authResponse;
    }

    private void validateRegistrationRequest(RegisterRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse email est requise");
        }

        if (!EMAIL_PATTERN.matcher(request.getEmail()).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }

        if (request.getPassword() == null || !PASSWORD_PATTERN.matcher(request.getPassword()).matches()) {
            throw new IllegalArgumentException(
                    "Le mot de passe doit contenir au moins 8 caractères, une majuscule, une minuscule et un chiffre"
            );
        }

        if (request.getNom() == null || request.getNom().trim().length() < 2) {
            throw new IllegalArgumentException("Le nom doit contenir au moins 2 caractères");
        }

        if (request.getPrenom() == null || request.getPrenom().trim().length() < 2) {
            throw new IllegalArgumentException("Le prénom doit contenir au moins 2 caractères");
        }
    }

    private void validateLoginRequest(LoginRequest request) {
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'adresse email est requise");
        }

        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            throw new IllegalArgumentException("Le mot de passe est requis");
        }
    }

    private String sanitizeInput(String input) {
        if (input == null) return null;
        return input.trim().replaceAll("[<>\"'%;()&+]", "");
    }

    private UserDTO mapToUserDTO(UserEntity user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setNom(user.getNom());
        userDTO.setPrenom(user.getPrenom());
        userDTO.setEmail(user.getEmail());
        userDTO.setAdresse(user.getAdresse());
        userDTO.setCodePostal(user.getCodePostal());
        userDTO.setVille(user.getVille());
        userDTO.setAvatar(user.getAvatar());
        return userDTO;
    }
}

