package com.example.ecommerce.controller.dto;


import com.example.ecommerce.ennumeration.Genre;
import com.example.ecommerce.ennumeration.Taille;
import com.example.ecommerce.entities.ProductEntity;
import lombok.Data; // Ou créez manuellement les getters/setters si vous n'utilisez pas Lombok
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Data // Génère les getters, setters, toString, equals, hashCode
@NoArgsConstructor // Génère un constructeur sans arguments
@AllArgsConstructor
@ToString // Génère un constructeur avec tous les arguments
public class ProductDTO {
    private int id;
    private String nom;
    private String description;
    private String marque;
    private Double prix;
    private String thumbnailUrl;
    private Genre genre;
    private Taille taille;
    private Integer categoryId; // On mettra seulement l'ID de la catégorie
    private String categoryNom; // Optionnel : si vous voulez le nom de la catégorie directement

    // Constructeur pour mapper de ProductEntity vers ProductDTO
    public ProductDTO(ProductEntity productEntity) {
        this.id = productEntity.getId();
        this.nom = productEntity.getNom();
        this.description = productEntity.getDescription();
        this.marque = productEntity.getMarque();
        this.prix = productEntity.getPrix();
        this.thumbnailUrl = productEntity.getThumbnailUrl();
        this.genre = productEntity.getGenre();
        this.taille = productEntity.getTaille();

        // Pour la catégorie, nous n'incluons que l'ID et le nom, pas l'objet CategoryEntity complet.
        // Cela évite la boucle de sérialisation et une surcharge de données.
        if (productEntity.getCategory() != null) {
            this.categoryId = productEntity.getCategory().getId();
            this.categoryNom = productEntity.getCategory().getNom(); // Si vous voulez le nom de la catégorie
        }
    }
}
