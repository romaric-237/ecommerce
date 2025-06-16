package com.example.ecommerce.security;

import com.example.ecommerce.entities.UserEntity;
import com.example.ecommerce.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + username));

        return UserPrincipal.create(userEntity);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(int id) {
        UserEntity utilisateur = userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'id: " + id));

        return UserPrincipal.create(utilisateur);
    }
}

