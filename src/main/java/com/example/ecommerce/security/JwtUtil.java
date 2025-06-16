package com.example.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.security.jwt.secret-key}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration}")
    private int jwtExpirationMs;

    @Value("${spring.security.jwt.refresh-expiration}")
    private int jwtRefreshExpirationMs;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Génère un token JWT à partir des détails de l'utilisateur
     */
    public String generateJwtToken(UserDetails userPrincipal, int userId, String role) {
        return generateTokenFromUsername(userPrincipal.getUsername(), userId, role, jwtExpirationMs);
    }

    /**
     * Génère un refresh token
     */
    public String generateRefreshToken(UserDetails userPrincipal, int userId, String role) {
        return generateTokenFromUsername(userPrincipal.getUsername(), userId, role, jwtRefreshExpirationMs);
    }

    /**
     * Génère un token avec des claims personnalisés
     */
    private String generateTokenFromUsername(String username, int userId, String role, int expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait le nom d'utilisateur du token
     */
    public String getUserNameFromJwtToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Extrait l'ID utilisateur du token
     */
    public int getUserIdFromJwtToken(String token) {
        String userIdStr = (String) getClaimFromToken(token, claims -> claims.get("userId"));
        return Integer.parseInt(userIdStr);
    }

    /**
     * Extrait le rôle du token
     */
    public String getRoleFromJwtToken(String token) {
        return (String) getClaimFromToken(token, claims -> claims.get("role"));
    }

    /**
     * Extrait la date d'expiration du token
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Extrait un claim spécifique du token
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrait tous les claims du token
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Vérifie si le token a expiré
     */
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Valide le token JWT
     */
    public Boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT mal formé: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expiré: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT non supporté: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Claims JWT vides: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur de validation du token JWT: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Valide le token par rapport à un utilisateur
     */
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUserNameFromJwtToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Extrait le token du header Authorization
     */
    public String extractTokenFromHeader(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    /**
     * Obtient le temps restant avant expiration en millisecondes
     */
    public long getTimeUntilExpiration(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.getTime() - new Date().getTime();
    }

    /**
     * Vérifie si le token doit être rafraîchi (expire dans moins de 5 minutes)
     */
    public boolean shouldRefreshToken(String token) {
        long timeUntilExpiration = getTimeUntilExpiration(token);
        return timeUntilExpiration < 300000; // 5 minutes en millisecondes
    }
}

