package com.example.ecommerce.controller.dto;

import lombok.Data;
import java.util.List;

@Data
public class CartResponseDTO {
    private Long id;
    private UserDTO utilisateur;
    private List<CartLineDTO> lignes;
    
    @Data
    public static class CartLineDTO {
        private Long id;
        private ProductDTO produit;
        private int quantite;
        private double prixUnitaire;
        private double prixTotal;
    }
    
    @Data
    public static class ProductDTO {
        private int id;
        private String nom;
        private String description;
        private String marque;
        private Double prix;
        private String thumbnailUrl;
        private String genre;
        private String taille;
        private CategoryDTO category;
    }
    
    @Data
    public static class CategoryDTO {
        private int id;
        private String nom;
        private String description;
    }
} 