package com.example.ecommerce.security;

import com.example.ecommerce.entities.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;


public class UserPrincipal implements UserDetails {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String motDePasse;

    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(int id, String nom, String prenom, String email, String motDePasse, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.authorities = authorities;
    }

    public static UserPrincipal create(UserEntity userEntity) {
        // Utiliser le rôle réel de l'utilisateur avec le préfixe ROLE_
        String role = "ROLE_" + userEntity.getRole().name();
        Collection<GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority(role)
        );

        return new UserPrincipal(
            userEntity.getId(),
            userEntity.getNom(),
            userEntity.getPrenom(),
            userEntity.getEmail(),
            userEntity.getPassword(),
            authorities
        );
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }


    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

