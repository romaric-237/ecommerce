package com.example.ecommerce.controller.dto;

import com.example.ecommerce.ennumeration.Genre;
import com.example.ecommerce.ennumeration.Taille;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDTO {
    
    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;
    
    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, max = 1000, message = "La description doit contenir entre 10 et 1000 caractères")
    private String description;
    
    @NotBlank(message = "La marque est obligatoire")
    @Size(min = 2, max = 50, message = "La marque doit contenir entre 2 et 50 caractères")
    private String marque;
    
    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    @DecimalMax(value = "999999.99", message = "Le prix ne peut pas dépasser 999999.99")
    private Double prix;
    
    @NotBlank(message = "L'URL de l'image est obligatoire")
    @Pattern(regexp = "https?://.*", message = "L'URL de l'image doit commencer par http:// ou https://")
    private String thumbnailUrl;
    
    @NotNull(message = "Le genre est obligatoire")
    private Genre genre;
    
    @NotNull(message = "La taille est obligatoire")
    private Taille taille;
    
    @NotNull(message = "L'ID de la catégorie est obligatoire")
    @Min(value = 1, message = "L'ID de la catégorie doit être positif")
    private Integer categoryId;
} 