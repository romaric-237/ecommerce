package com.example.ecommerce.entity;

import jakarta.persistence.*;

@Entity
@Table(name="categorie")
public class Categorie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="nom")
    private String nom;
    @Column(name="description")
    private String description;
}
