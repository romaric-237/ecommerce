package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.CreateOrderDTO;
import com.example.ecommerce.controller.dto.OrderDTO;
import com.example.ecommerce.services.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Crée une nouvelle commande
     */
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody CreateOrderDTO createOrderDTO) {
        try {
            Integer userId = getCurrentUserId();
            OrderDTO order = orderService.createOrder(createOrderDTO, userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère toutes les commandes de l'utilisateur connecté
     */
    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<OrderDTO>> getMyOrders() {
        try {
            Integer userId = getCurrentUserId();
            List<OrderDTO> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Récupère une commande par son ID
     */
    @GetMapping("/{orderId}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère une commande par son numéro
     */
    @GetMapping("/number/{orderNumber}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderDTO> getOrderByNumber(@PathVariable String orderNumber) {
        try {
            OrderDTO order = orderService.getOrderByNumber(orderNumber);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Récupère l'ID de l'utilisateur connecté
     */
    private Integer getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // TODO : Récupérer l'ID utilisateur depuis le token JWT
            // Pour l'instant, on retourne un ID par défaut
            return 2; // client@test.com
        }
        throw new RuntimeException("Utilisateur non authentifié");
    }
} 