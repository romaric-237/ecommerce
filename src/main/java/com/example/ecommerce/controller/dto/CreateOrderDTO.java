package com.example.ecommerce.controller.dto;

import com.example.ecommerce.ennumeration.PaymentMethod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDTO {
    
    @NotNull(message = "La méthode de paiement est obligatoire")
    private PaymentMethod paymentMethod;
    
    // Adresse de livraison (structure envoyée par le frontend)
    @NotNull(message = "L'adresse de livraison est obligatoire")
    private ShippingAddressDTO shippingAddress;
    
    // Articles du panier
    private List<OrderItemRequestDTO> items;
    
    // Montant total
    @NotNull(message = "Le montant total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le montant total doit être supérieur à 0")
    private Double totalAmount;
    
    // Commentaires optionnels
    @Size(max = 1000, message = "Les commentaires ne peuvent pas dépasser 1000 caractères")
    private String comments;
    
    // Validation du panier (optionnel)
    private Integer cartId;
    
    // DTO pour l'adresse de livraison
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShippingAddressDTO {
        @NotBlank(message = "Le prénom est obligatoire")
        private String firstName;
        
        @NotBlank(message = "Le nom est obligatoire")
        private String lastName;
        
        @NotBlank(message = "L'adresse est obligatoire")
        private String address;
        
        @NotBlank(message = "La ville est obligatoire")
        private String city;
        
        @NotBlank(message = "Le code postal est obligatoire")
        @Pattern(regexp = "^[0-9]{5}$", message = "Le code postal doit contenir 5 chiffres")
        private String postalCode;
        
        @NotBlank(message = "Le pays est obligatoire")
        private String country;
        
        @Pattern(regexp = "^[0-9+\\-\\s()]{10,20}$", message = "Le numéro de téléphone n'est pas valide")
        private String phone;
    }
    
    // DTO pour les articles de commande
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequestDTO {
        @NotNull(message = "L'ID du produit est obligatoire")
        private Integer productId;
        
        @NotNull(message = "La quantité est obligatoire")
        @Min(value = 1, message = "La quantité doit être au moins 1")
        private Integer quantity;
        
        @NotNull(message = "Le prix unitaire est obligatoire")
        @DecimalMin(value = "0.01", message = "Le prix unitaire doit être supérieur à 0")
        private Double unitPrice;
    }
} 