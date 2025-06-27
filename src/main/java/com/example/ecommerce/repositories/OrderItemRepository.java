package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    
    /**
     * Trouve tous les articles d'une commande
     */
    List<OrderItem> findByOrderIdOrderByProductNom(Long orderId);
    
    /**
     * Trouve tous les articles d'un produit
     */
    List<OrderItem> findByProductId(Long productId);
    
    /**
     * Compte le nombre d'articles vendus pour un produit
     */
    long countByProductId(Long productId);
    
    /**
     * Trouve les articles les plus vendus
     */
    @Query("SELECT oi.product.id, COUNT(oi) as total FROM OrderItem oi GROUP BY oi.product.id ORDER BY total DESC")
    List<Object[]> findMostSoldProducts();
    
    /**
     * Trouve les articles d'une commande avec les détails du produit
     */
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderIdWithProduct(@Param("orderId") Long orderId);
} 