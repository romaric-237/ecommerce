package com.example.ecommerce.entities;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="user", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name="nom")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(name="prenom")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    @Column(name="email", unique = true)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Column(name="password")
    private String password;

    @NotBlank(message = "L'adresse est obligatoire")
    @Column(name="adresse")
    private String adresse;

    @NotBlank(message = "Le code postal est obligatoire")
    @Column(name="code_postal")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire")
    @Column(name="ville")
    private String ville;

    @Column(name="telephone")
    private String telephone;
}
