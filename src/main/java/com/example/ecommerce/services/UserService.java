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
            throw new IllegalArgumentException("Un compte avec cette adresse email existe déjà");
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
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));
    }

    public UserEntity findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'ID : " + id));
    }

    @Transactional
    public UserEntity updateUser(Integer id, UserRegistrationDTO dto) {
        UserEntity user = findById(id);

        // Si email changé → vérifier l'unicité
        if (!user.getEmail().equalsIgnoreCase(dto.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("L'adresse email est déjà utilisée");
        }

        // Mise à jour des champs
        user.setNom(dto.getNom());
        user.setPrenom(dto.getPrenom());
        user.setEmail(dto.getEmail());
        user.setAdresse(dto.getAdresse());
        user.setCodePostal(dto.getCodePostal());
        user.setVille(dto.getVille());
        user.setTelephone(dto.getTelephone());

        // Si un nouveau mot de passe est fourni, le mettre à jour
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return userRepository.save(user);
    }
}
