package com.example.ecommerce.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequest {
    @NotBlank(message = "Le refresh token est requis")
    private String refreshToken;

    public @NotBlank(message = "Le refresh token est requis") String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(@NotBlank(message = "Le refresh token est requis") String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

