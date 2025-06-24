package com.example.ecommerce.entities;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonManagedReference;
@Data
@Entity
public class Panier {
    // Getters et setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserEntity utilisateur;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LignePanier> lignes = new ArrayList<>();

    public void setId(Long id) {
        this.id = id;
    }

    public void setUtilisateur(UserEntity utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setLignes(List<LignePanier> lignes) {
        this.lignes = lignes;
    }
} 