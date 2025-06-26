package com.example.ecommerce.controller.dto;

import com.example.ecommerce.ennumeration.Genre;
import com.example.ecommerce.ennumeration.Taille;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateResponseDTO {
    
    private int id;
    private String nom;
    private String description;
    private String marque;
    private Double prix;
    private String thumbnailUrl;
    private Genre genre;
    private Taille taille;
    private CategoryDTO category;
    private LocalDateTime updatedAt;
    private String message;
    
    public ProductUpdateResponseDTO(int id, String nom, String description, String marque, 
                                   Double prix, String thumbnailUrl, Genre genre, Taille taille, 
                                   CategoryDTO category, LocalDateTime updatedAt) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.marque = marque;
        this.prix = prix;
        this.thumbnailUrl = thumbnailUrl;
        this.genre = genre;
        this.taille = taille;
        this.category = category;
        this.updatedAt = updatedAt;
        this.message = "Produit mis à jour avec succès";
    }
} 