package com.example.ecommerce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            // Vérifier si le header Authorization est présent et bien formaté
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            
            // Vérifier que le token n'est pas vide
            if (jwt.trim().isEmpty()) {
                handleAuthenticationError(response, "Token vide", HttpStatus.UNAUTHORIZED);
                return;
            }

            // Vérifier si c'est un refresh token (ne doit pas être utilisé pour l'authentification)
            if (jwtService.isRefreshToken(jwt)) {
                handleAuthenticationError(response, "Refresh token ne peut pas être utilisé pour l'authentification", HttpStatus.UNAUTHORIZED);
                return;
            }

            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("Utilisateur authentifié avec succès : {}", userEmail);
                } else {
                    log.warn("Token invalide pour l'utilisateur : {}", userEmail);
                    handleAuthenticationError(response, "Token invalide ou expiré", HttpStatus.UNAUTHORIZED);
                    return;
                }
            }
            
            filterChain.doFilter(request, response);
            
        } catch (ExpiredJwtException e) {
            log.debug("Token expiré : {}", e.getMessage());
            handleAuthenticationError(response, "Token expiré", HttpStatus.UNAUTHORIZED);
        } catch (SecurityException | JwtException e) {
            log.warn("Erreur de sécurité JWT : {}", e.getMessage());
            handleAuthenticationError(response, "Token invalide", HttpStatus.UNAUTHORIZED);
        } catch (UsernameNotFoundException e) {
            log.warn("Utilisateur non trouvé : {}", e.getMessage());
            handleAuthenticationError(response, "Utilisateur non trouvé", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Erreur inattendue dans le filtre JWT : {}", e.getMessage(), e);
            handleAuthenticationError(response, "Erreur d'authentification", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private void handleAuthenticationError(HttpServletResponse response, String message, HttpStatus status) 
            throws IOException {
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> errorResponse = Map.of(
            "error", "Erreur d'authentification",
            "message", message,
            "status", status.value(),
            "timestamp", System.currentTimeMillis()
        );
        
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // Ne pas filtrer les routes publiques
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/login") || 
               path.startsWith("/api/auth/register") ||
               path.startsWith("/api/auth/refresh") ||
               path.equals("/api/auth/validate") ||
               path.startsWith("/api/public/");
    }
}

