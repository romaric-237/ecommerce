package com.example.ecommerce.repositories;

import com.example.ecommerce.entities.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    boolean existsByEmail(String email); // Pour vérifier si un email est déjà utilisé
    Optional<UserEntity> findByEmail(String email);

}