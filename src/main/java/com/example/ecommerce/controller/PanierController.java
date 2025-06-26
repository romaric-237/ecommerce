package com.example.ecommerce.controller;

import com.example.ecommerce.entities.Panier;
import com.example.ecommerce.services.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('CLIENT')") // Seuls les clients peuvent accéder au panier
public class PanierController {

    @Autowired
    private PanierService panierService;

    // Pour simplifier, on passe l'id utilisateur en paramètre (à sécuriser avec l'authentification réelle)

    @GetMapping("/user/{userId}")
    public ResponseEntity<Panier> getPanier(@PathVariable Integer userId) {
        Panier panier = panierService.getOrCreatePanier(userId);
        return ResponseEntity.ok(panier);
    }

    @PostMapping("/add")
    public ResponseEntity<Panier> addProductToCart(@RequestParam Integer userId,
                                                   @RequestParam Integer productId,
                                                   @RequestParam(defaultValue = "1") int quantite) {
        Panier panier = panierService.addProductToPanier(userId, productId, quantite);
        return ResponseEntity.ok(panier);
    }

    @PutMapping("/update")
    public ResponseEntity<Panier> updateProductQuantity(@RequestParam Integer userId,
                                                       @RequestParam Integer productId,
                                                       @RequestParam int quantite) {
        Panier panier = panierService.updateProductQuantity(userId, productId, quantite);
        return ResponseEntity.ok(panier);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Panier> removeProductFromCart(@RequestParam Integer userId,
                                                       @RequestParam Integer productId) {
        Panier panier = panierService.removeProductFromPanier(userId, productId);
        return ResponseEntity.ok(panier);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Panier> clearCart(@RequestParam Integer userId) {
        Panier panier = panierService.clearPanier(userId);
        return ResponseEntity.ok(panier);
    }
} 