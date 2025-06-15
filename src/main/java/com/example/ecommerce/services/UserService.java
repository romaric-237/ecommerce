package com.example.ecommerce.services;

import com.example.ecommerce.controller.dto.UserRegistrationDTO;
import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }

        UserEntity user = new UserEntity();
        user.setNom(registrationDTO.getNom());
        user.setPrenom(registrationDTO.getPrenom());
        user.setEmail(registrationDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
        user.setAdresse(registrationDTO.getAdresse());
        user.setCodePostal(registrationDTO.getCodePostal());
        user.setVille(registrationDTO.getVille());
        user.setTelephone(registrationDTO.getTelephone());

        return userRepository.save(user);
    }

    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
} 