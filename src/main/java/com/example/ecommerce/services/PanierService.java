package com.example.ecommerce.services;

import com.example.ecommerce.entities.LignePanier;
import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.LignePanierRepository;
import com.example.ecommerce.repositories.PanierRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PanierService {
    @Autowired
    private PanierRepository panierRepository;
    @Autowired
    private LignePanierRepository lignePanierRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

    /**
     * Récupérer le panier de l'utilisateur (le créer s'il n'existe pas)
     */
    public Panier getOrCreatePanier(Integer userId) {
        Panier panier = panierRepository.findByUtilisateurId(userId);
        if (panier == null) {
            panier = new Panier();
            UserEntity user = userRepository.findById(userId).orElseThrow();
            panier.setUtilisateur(user);
            panierRepository.save(panier);
        }
        return panier;
    }

    /**
     * Ajouter un produit au panier
     */
    @Transactional
    public Panier addProductToPanier(Integer userId, Integer productId, int quantite) {
        Panier panier = getOrCreatePanier(userId);
        Optional<LignePanier> ligneOpt = panier.getLignes().stream()
                .filter(l -> l.getProduit().getId() == productId)
                .findFirst();
        if (ligneOpt.isPresent()) {
            LignePanier ligne = ligneOpt.get();
            ligne.setQuantite(ligne.getQuantite() + quantite);
            lignePanierRepository.save(ligne);
        } else {
            ProductEntity produit = productRepository.findById(productId).orElseThrow();
            LignePanier ligne = new LignePanier();
            ligne.setPanier(panier);
            ligne.setProduit(produit);
            ligne.setQuantite(quantite);
            ligne.setPrixUnitaire(produit.getPrix());
            lignePanierRepository.save(ligne);
            panier.getLignes().add(ligne);
        }
        panierRepository.save(panier);
        return panier;
    }

    /**
     * Modifier la quantité d'un produit dans le panier
     */
    @Transactional
    public Panier updateProductQuantity(Integer userId, Integer productId, int quantite) {
        Panier panier = getOrCreatePanier(userId);
        panier.getLignes().stream()
                .filter(l -> l.getProduit().getId() == productId)
                .findFirst()
                .ifPresent(ligne -> {
                    ligne.setQuantite(quantite);
                    lignePanierRepository.save(ligne);
                });
        panierRepository.save(panier);
        return panier;
    }

    /**
     * Supprimer un produit du panier
     */
    @Transactional
    public Panier removeProductFromPanier(Integer userId, Integer productId) {
        Panier panier = getOrCreatePanier(userId);
        panier.getLignes().removeIf(ligne -> {
            boolean toRemove = ligne.getProduit().getId() == productId;
            if (toRemove) {
                lignePanierRepository.delete(ligne);
            }
            return toRemove;
        });
        panierRepository.save(panier);
        return panier;
    }

    /**
     * Vider le panier
     */
    @Transactional
    public Panier clearPanier(Integer userId) {
        Panier panier = getOrCreatePanier(userId);
        panier.getLignes().forEach(lignePanierRepository::delete);
        panier.getLignes().clear();
        panierRepository.save(panier);
        return panier;
    }
} 