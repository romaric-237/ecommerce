package com.example.ecommerce.entities;
import lombok.Data;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Data
@Entity
public class LignePanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "panier_id")
    @JsonBackReference
    private Panier panier;

    @ManyToOne
    private ProductEntity produit;

    private int quantite;
    private double prixUnitaire;

    // Getters et setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Panier getPanier() {
        return panier;
    }
    public void setPanier(Panier panier) {
        this.panier = panier;
    }
    public ProductEntity getProduit() {
        return produit;
    }
    public void setProduit(ProductEntity produit) {
        this.produit = produit;
    }
    public int getQuantite() {
        return quantite;
    }
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
} 