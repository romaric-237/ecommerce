package com.example.ecommerce.entity;

import com.example.ecommerce.ennumeration.Taille;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="produit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    @JsonBackReference
    private Categorie categorie;

}
