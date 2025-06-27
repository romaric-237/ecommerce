package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.Order;
import com.example.ecommerce.ennumeration.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    /**
     * Trouve toutes les commandes d'un utilisateur
     */
    List<Order> findByUserIdOrderByOrderDateDesc(Integer userId);
    
    /**
     * Trouve une commande par son numéro
     */
    Optional<Order> findByOrderNumber(String orderNumber);
    
    /**
     * Trouve toutes les commandes par statut
     */
    List<Order> findByStatusOrderByOrderDateDesc(OrderStatus status);
    
    /**
     * Trouve toutes les commandes d'un utilisateur par statut
     */
    List<Order> findByUserIdAndStatusOrderByOrderDateDesc(Integer userId, OrderStatus status);
    
    /**
     * Trouve les commandes créées entre deux dates
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findOrdersBetweenDates(@Param("startDate") LocalDateTime startDate, 
                                      @Param("endDate") LocalDateTime endDate);
    
    /**
     * Trouve les commandes d'un utilisateur entre deux dates
     */
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findUserOrdersBetweenDates(@Param("userId") Integer userId,
                                          @Param("startDate") LocalDateTime startDate, 
                                          @Param("endDate") LocalDateTime endDate);
    
    /**
     * Compte le nombre de commandes par statut
     */
    long countByStatus(OrderStatus status);
    
    /**
     * Compte le nombre de commandes d'un utilisateur
     */
    long countByUserId(Integer userId);
    
    /**
     * Trouve les commandes avec un montant supérieur à une valeur
     */
    List<Order> findByTotalAmountGreaterThanOrderByTotalAmountDesc(Double amount);
    
    /**
     * Trouve les commandes récentes (derniers 30 jours)
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt >= :thirtyDaysAgo ORDER BY o.createdAt DESC")
    List<Order> findRecentOrders(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
} 