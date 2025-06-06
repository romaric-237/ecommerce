package com.example.ecommerce.controller.dto;

import com.example.ecommerce.entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class CategoryDTO {
    private int id;
    private String nom;
    private String description;
    // Optionnel: List de ProductDTO si vous voulez des détails limités
    // private List<ProductDTO> produits; // Ou juste les IDs des produits

    // Constructeur pour mapper de CategoryEntity vers CategoryDTO
    public CategoryDTO(CategoryEntity category) {
        this.id = category.getId();
        this.nom = category.getNom();
        this.description = category.getDescription();
        // Si vous voulez inclure les produits, vous devrez les mapper aussi:
        // this.produits = category.getProduitentity().stream()
        //     .map(ProductDTO::new) // Assurez-vous d'avoir un ProductDTO approprié
        //     .collect(Collectors.toList());
    }

}