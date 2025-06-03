package com.example.ecommerce.entity;

import com.example.ecommerce.ennumeration.Taille;
import jakarta.persistence.*;

@Entity
@Table(name="produit")
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="nom")
    private String nom;
    @Column(name="description")
    private String description;
    @Column(name="marque")
    private String marque;
    @Column(name="prix")
    private Double prix;

    @Enumerated(EnumType.STRING)
    private Taille taille;
}
