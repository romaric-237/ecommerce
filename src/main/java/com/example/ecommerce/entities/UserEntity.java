package com.example.ecommerce.entities;
import com.example.ecommerce.ennumeration.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@Entity
@Table(name="user", uniqueConstraints = {
    @UniqueConstraint(columnNames = "email")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity  {
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
    private String avatar;

    @Column(nullable = false)
    private boolean enabled = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.CLIENT;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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
