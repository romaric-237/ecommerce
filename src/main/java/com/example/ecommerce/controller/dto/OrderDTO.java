package com.example.ecommerce.controller.dto;

import com.example.ecommerce.ennumeration.OrderStatus;
import com.example.ecommerce.ennumeration.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String orderNumber;
    private Integer userId;
    private String userEmail;
    private String userName;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private PaymentMethod paymentMethod;
    private Double totalAmount;
    
    // Adresse de facturation
    private String billingAddress;
    private String billingPostalCode;
    private String billingCity;
    
    // Adresse de livraison
    private String shippingAddress;
    private String shippingPostalCode;
    private String shippingCity;
    private String shippingCountry;
    private String shippingPhone;
    private String shippingEmail;
    
    // Commentaires
    private String comments;
    
    // Articles de la commande
    private List<OrderItemDTO> orderItems;
    
    // Champs d'audit
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 