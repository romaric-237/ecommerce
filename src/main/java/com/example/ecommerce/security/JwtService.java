package com.example.ecommerce.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

@Service
public class JwtService {
    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.security.jwt.expiration:900000}") // 15 minutes par défaut
    private long accessTokenExpiration;

    @Value("${spring.security.jwt.refresh-expiration:604800000}") // 7 jours par défaut
    private long refreshTokenExpiration;

    // Blacklist des tokens révoqués
    private final Set<String> revokedTokens = ConcurrentHashMap.newKeySet();

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            log.error("Erreur lors de l'extraction du nom d'utilisateur : {}", e.getMessage());
            throw new SecurityException("Token invalide", e);
        }
    }

    public String extractTokenId(String token) {
        try {
            return extractClaim(token, Claims::getId);
        } catch (JwtException e) {
            log.error("Erreur lors de l'extraction de l'ID du token : {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails, accessTokenExpiration, "ACCESS");
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "REFRESH");
        return generateToken(claims, userDetails, refreshTokenExpiration, "REFRESH");
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration, String tokenType) {
        String tokenId = UUID.randomUUID().toString();
        
        return Jwts.builder()
                .setClaims(extraClaims)
                .setId(tokenId)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .setIssuer("ecommerce-app")
                .setAudience("ecommerce-users")
                .claim("type", tokenType)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512) // HS512 plus sécurisé
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            if (isTokenRevoked(token)) {
                log.warn("Tentative d'utilisation d'un token révoqué");
                return false;
            }
            
            final String username = extractUsername(token);
            return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
        } catch (Exception e) {
            log.error("Erreur lors de la validation du token : {}", e.getMessage());
            return false;
        }
    }

    public boolean isRefreshToken(String token) {
        try {
            String tokenType = extractClaim(token, claims -> claims.get("type", String.class));
            return "REFRESH".equals(tokenType);
        } catch (Exception e) {
            log.error("Erreur lors de la vérification du type de token : {}", e.getMessage());
            return false;
        }
    }

    public void revokeToken(String token) {
        try {
            String tokenId = extractTokenId(token);
            if (tokenId != null) {
                revokedTokens.add(tokenId);
                log.info("Token révoqué avec succès : {}", tokenId);
            }
        } catch (Exception e) {
            log.error("Erreur lors de la révocation du token : {}", e.getMessage());
        }
    }

    public boolean isTokenRevoked(String token) {
        try {
            String tokenId = extractTokenId(token);
            return tokenId != null && revokedTokens.contains(tokenId);
        } catch (Exception e) {
            log.error("Erreur lors de la vérification de révocation : {}", e.getMessage());
            return true; // En cas d'erreur, considérer le token comme révoqué par sécurité
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            log.debug("Token expiré : {}", e.getMessage());
            return true;
        } catch (JwtException e) {
            log.error("Erreur lors de la vérification d'expiration : {}", e.getMessage());
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .requireIssuer("ecommerce-app")
                    .requireAudience("ecommerce-users")
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.debug("Token expiré : {}", e.getMessage());
            throw e;
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT non supporté : {}", e.getMessage());
            throw new SecurityException("Token non supporté", e);
        } catch (MalformedJwtException e) {
            log.error("Token JWT malformé : {}", e.getMessage());
            throw new SecurityException("Token malformé", e);
        } catch (SignatureException e) {
            log.error("Signature JWT invalide : {}", e.getMessage());
            throw new SecurityException("Signature invalide", e);
        } catch (IllegalArgumentException e) {
            log.error("Token JWT vide ou null : {}", e.getMessage());
            throw new SecurityException("Token vide", e);
        }
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Méthode pour nettoyer périodiquement les tokens expirés de la blacklist
    public void cleanupExpiredTokens() {
        log.info("Nettoyage des tokens révoqués - Taille actuelle : {}", revokedTokens.size());
        // Implémentation future : supprimer les tokens expirés de la blacklist
    }
}

