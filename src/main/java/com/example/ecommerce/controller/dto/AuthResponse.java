package com.example.ecommerce.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private UserDTO user;
    private String tokenType = "Bearer";
    private long expiresIn; // Durée de vie en secondes
    
    // Setters manuels pour compatibilité
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
    
    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    
    public void setUser(UserDTO user) {
        this.user = user;
    }
    
    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }
    
    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }
    
    // Getters manuels pour compatibilité
    public String getAccessToken() {
        return accessToken;
    }
    
    public String getRefreshToken() {
        return refreshToken;
    }
    
    public UserDTO getUser() {
        return user;
    }
    
    public String getTokenType() {
        return tokenType;
    }
    
    public long getExpiresIn() {
        return expiresIn;
    }
}
