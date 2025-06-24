package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {
    Panier findByUtilisateurId(Integer utilisateurId);
} 