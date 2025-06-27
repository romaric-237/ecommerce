package com.example.ecommerce.services;

import com.example.ecommerce.controller.dto.CreateOrderDTO;
import com.example.ecommerce.controller.dto.OrderDTO;
import com.example.ecommerce.controller.dto.OrderItemDTO;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.OrderItem;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.exception.OrderNotFoundException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.repositories.OrderRepository;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    // private final PaymentService paymentService; // à implémenter
    // private final StockService stockService; // à implémenter

    /**
     * Crée une nouvelle commande à partir du panier et des infos de livraison/paiement
     */
    @Transactional
    public OrderDTO createOrder(CreateOrderDTO createOrderDTO, Integer userId) {
        // 1. Récupérer l'utilisateur
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé: " + userId));
        
        // 2. Créer la commande
        Order order = new Order();
        order.setUser(user);
        order.setPaymentMethod(createOrderDTO.getPaymentMethod());
        
        // 3. Adresse de livraison depuis le DTO
        CreateOrderDTO.ShippingAddressDTO shippingAddress = createOrderDTO.getShippingAddress();
        order.setShippingAddress(shippingAddress.getAddress());
        order.setShippingPostalCode(shippingAddress.getPostalCode());
        order.setShippingCity(shippingAddress.getCity());
        order.setShippingCountry(shippingAddress.getCountry());
        order.setShippingPhone(shippingAddress.getPhone());
        order.setShippingEmail(user.getEmail()); // Utiliser l'email de l'utilisateur
        
        // 4. Adresse de facturation (utiliser celle de l'utilisateur)
        order.setBillingAddress(user.getAdresse());
        order.setBillingPostalCode(user.getCodePostal());
        order.setBillingCity(user.getVille());
        
        // 5. Commentaires
        order.setComments(createOrderDTO.getComments());
        
        // 6. Créer les articles de commande à partir du panier
        if (createOrderDTO.getItems() != null && !createOrderDTO.getItems().isEmpty()) {
            for (CreateOrderDTO.OrderItemRequestDTO itemRequest : createOrderDTO.getItems()) {
                ProductEntity product = productRepository.findById(itemRequest.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException(itemRequest.getProductId()));
                
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                orderItem.setQuantity(itemRequest.getQuantity());
                orderItem.setPrice(itemRequest.getUnitPrice());
                
                order.addOrderItem(orderItem);
            }
        } else {
            // Fallback : créer une commande de test avec un produit
            ProductEntity product = productRepository.findById(1)
                    .orElseThrow(() -> new ProductNotFoundException(1));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(1);
            orderItem.setPrice(product.getPrix());
            
            order.addOrderItem(orderItem);
        }
        
        // 7. Sauvegarder la commande
        Order savedOrder = orderRepository.save(order);
        
        // 8. Retourner le DTO
        return toDTO(savedOrder);
    }

    /**
     * Récupère toutes les commandes d'un utilisateur
     */
    public List<OrderDTO> getOrdersByUser(Integer userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Récupère une commande par son ID
     */
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return toDTO(order);
    }

    /**
     * Récupère une commande par son numéro
     */
    public OrderDTO getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        return toDTO(order);
    }

    /**
     * Convertit une entité Order en DTO
     */
    public OrderDTO toDTO(Order order) {
        List<OrderItemDTO> orderItemDTOs = order.getOrderItems().stream()
                .map(this::toOrderItemDTO)
                .collect(Collectors.toList());
        
        return OrderDTO.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .userId(order.getUser().getId())
                .userEmail(order.getUser().getEmail())
                .userName(order.getUser().getPrenom() + " " + order.getUser().getNom())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .paymentMethod(order.getPaymentMethod())
                .totalAmount(order.getTotalAmount())
                .billingAddress(order.getBillingAddress())
                .billingPostalCode(order.getBillingPostalCode())
                .billingCity(order.getBillingCity())
                .shippingAddress(order.getShippingAddress())
                .shippingPostalCode(order.getShippingPostalCode())
                .shippingCity(order.getShippingCity())
                .shippingCountry(order.getShippingCountry())
                .shippingPhone(order.getShippingPhone())
                .shippingEmail(order.getShippingEmail())
                .comments(order.getComments())
                .orderItems(orderItemDTOs)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
    
    /**
     * Convertit une entité OrderItem en DTO
     */
    private OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getNom())
                .productImage(orderItem.getProduct().getThumbnailUrl())
                .productBrand(orderItem.getProduct().getMarque())
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .totalPrice(orderItem.getPrice() * orderItem.getQuantity())
                .build();
    }
} 