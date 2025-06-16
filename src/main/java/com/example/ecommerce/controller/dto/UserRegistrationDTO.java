package com.example.ecommerce.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationDTO {
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "Le code postal est obligatoire")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    private String telephone;

    public @NotBlank(message = "Le nom est obligatoire") String getNom() {
        return nom;
    }

    public void setNom(@NotBlank(message = "Le nom est obligatoire") String nom) {
        this.nom = nom;
    }

    public @NotBlank(message = "Le prénom est obligatoire") String getPrenom() {
        return prenom;
    }

    public void setPrenom(@NotBlank(message = "Le prénom est obligatoire") String prenom) {
        this.prenom = prenom;
    }

    public @NotBlank(message = "L'email est obligatoire") @Email(message = "L'email doit être valide") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "L'email est obligatoire") @Email(message = "L'email doit être valide") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Le mot de passe est obligatoire") @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Le mot de passe est obligatoire") @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères") String password) {
        this.password = password;
    }

    public @NotBlank(message = "L'adresse est obligatoire") String getAdresse() {
        return adresse;
    }

    public void setAdresse(@NotBlank(message = "L'adresse est obligatoire") String adresse) {
        this.adresse = adresse;
    }

    public @NotBlank(message = "Le code postal est obligatoire") String getCodePostal() {
        return codePostal;
    }

    public void setCodePostal(@NotBlank(message = "Le code postal est obligatoire") String codePostal) {
        this.codePostal = codePostal;
    }

    public @NotBlank(message = "La ville est obligatoire") String getVille() {
        return ville;
    }

    public void setVille(@NotBlank(message = "La ville est obligatoire") String ville) {
        this.ville = ville;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}