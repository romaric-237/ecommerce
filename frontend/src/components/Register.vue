<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>Inscription</h2>
      <form @submit.prevent="handleRegister" class="auth-form">
        <div class="form-group">
          <label for="nom">Nom</label>
          <input 
            type="text" 
            id="nom" 
            v-model="nom" 
            :class="{ 'error': errors.nom, 'valid': nom && !errors.nom }"
            @blur="validateNom"
            @input="clearError('nom')"
            placeholder="Votre nom"
          >
          <span v-if="errors.nom" class="field-error">{{ errors.nom }}</span>
        </div>
        <div class="form-group">
          <label for="prenom">Prénom</label>
          <input 
            type="text" 
            id="prenom" 
            v-model="prenom" 
            :class="{ 'error': errors.prenom, 'valid': prenom && !errors.prenom }"
            @blur="validatePrenom"
            @input="clearError('prenom')"
            placeholder="Votre prénom"
          >
          <span v-if="errors.prenom" class="field-error">{{ errors.prenom }}</span>
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input 
            type="email" 
            id="email" 
            v-model="email" 
            :class="{ 'error': errors.email, 'valid': email && !errors.email }"
            @blur="validateEmail"
            @input="clearError('email')"
            placeholder="Votre email"
          >
          <span v-if="errors.email" class="field-error">{{ errors.email }}</span>
        </div>
        <div class="form-group">
          <label for="adresse">Adresse</label>
          <input 
            type="text" 
            id="adresse" 
            v-model="adresse" 
            :class="{ 'error': errors.adresse, 'valid': adresse && !errors.adresse }"
            @blur="validateAdresse"
            @input="clearError('adresse')"
            placeholder="Votre adresse"
          >
          <span v-if="errors.adresse" class="field-error">{{ errors.adresse }}</span>
        </div>
        <div class="form-row">
          <div class="form-group">
            <label for="codePostal">Code Postal</label>
            <input 
              type="text" 
              id="codePostal" 
              v-model="codePostal" 
              :class="{ 'error': errors.codePostal, 'valid': codePostal && !errors.codePostal }"
              @blur="validateCodePostal"
              @input="clearError('codePostal')"
              placeholder="Code postal"
            >
            <span v-if="errors.codePostal" class="field-error">{{ errors.codePostal }}</span>
          </div>
          <div class="form-group">
            <label for="ville">Ville</label>
            <input 
              type="text" 
              id="ville" 
              v-model="ville" 
              :class="{ 'error': errors.ville, 'valid': ville && !errors.ville }"
              @blur="validateVille"
              @input="clearError('ville')"
              placeholder="Ville"
            >
            <span v-if="errors.ville" class="field-error">{{ errors.ville }}</span>
          </div>
        </div>
        <div class="form-group">
          <label for="password">Mot de passe</label>
          <input 
            type="password" 
            id="password" 
            v-model="password" 
            :class="{ 'error': errors.password, 'valid': password && !errors.password }"
            @blur="validatePassword"
            @input="clearError('password')"
            placeholder="Votre mot de passe"
          >
          <span v-if="errors.password" class="field-error">{{ errors.password }}</span>
          <div v-if="password" class="password-strength">
            <div class="strength-bar" :class="passwordStrength.class">
              <div class="strength-fill" :style="{ width: passwordStrength.width }"></div>
            </div>
            <span class="strength-text">{{ passwordStrength.text }}</span>
          </div>
        </div>
        <div class="form-group">
          <label for="confirmPassword">Confirmer le mot de passe</label>
          <input 
            type="password" 
            id="confirmPassword" 
            v-model="confirmPassword" 
            :class="{ 'error': errors.confirmPassword, 'valid': confirmPassword && !errors.confirmPassword }"
            @blur="validateConfirmPassword"
            @input="clearError('confirmPassword')"
            placeholder="Confirmez votre mot de passe"
          >
          <span v-if="errors.confirmPassword" class="field-error">{{ errors.confirmPassword }}</span>
        </div>
        <div v-if="generalError" class="error-message">
          <i class="error-icon">⚠️</i>
          {{ generalError }}
        </div>
        <div v-if="successMessage" class="success-message">
          <i class="success-icon">✅</i>
          {{ successMessage }}
        </div>
        <button 
          type="submit" 
          class="auth-button" 
          :disabled="loading || !isFormValid"
          :class="{ 'loading': loading }"
        >
          <span v-if="loading" class="spinner"></span>
          {{ loading ? 'Inscription...' : 'S\'inscrire' }}
        </button>
        <p class="auth-switch">
          Déjà un compte ? 
          <router-link to="/login" class="switch-link">Se connecter</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from '@/services/authService';

export default {
  name: 'Register',
  data() {
    return {
      nom: '',
      prenom: '',
      email: '',
      adresse: '',
      codePostal: '',
      ville: '',
      password: '',
      confirmPassword: '',
      errors: {},
      generalError: '',
      successMessage: '',
      loading: false
    };
  },
  computed: {
    isFormValid() {
      return this.nom.trim() && 
             this.prenom.trim() && 
             this.email.trim() && 
             this.adresse.trim() && 
             this.codePostal.trim() && 
             this.ville.trim() && 
             this.password && 
             this.confirmPassword && 
             Object.keys(this.errors).length === 0;
    },
    
    passwordStrength() {
      const password = this.password;
      let score = 0;
      let feedback = 'Très faible';
      
      if (password.length >= 8) score++;
      if (/[a-z]/.test(password)) score++;
      if (/[A-Z]/.test(password)) score++;
      if (/\d/.test(password)) score++;
      if (/[^\w\s]/.test(password)) score++;
      
      switch (score) {
        case 0:
        case 1:
          return { class: 'very-weak', width: '20%', text: 'Très faible' };
        case 2:
          return { class: 'weak', width: '40%', text: 'Faible' };
        case 3:
          return { class: 'medium', width: '60%', text: 'Moyen' };
        case 4:
          return { class: 'strong', width: '80%', text: 'Fort' };
        case 5:
          return { class: 'very-strong', width: '100%', text: 'Très fort' };
        default:
          return { class: 'very-weak', width: '20%', text: 'Très faible' };
      }
    }
  },
  methods: {
    validateNom() {
      if (!this.nom.trim()) {
        this.errors.nom = 'Le nom est requis';
      } else if (this.nom.trim().length < 2) {
        this.errors.nom = 'Le nom doit contenir au moins 2 caractères';
      } else {
        delete this.errors.nom;
      }
    },
    
    validatePrenom() {
      if (!this.prenom.trim()) {
        this.errors.prenom = 'Le prénom est requis';
      } else if (this.prenom.trim().length < 2) {
        this.errors.prenom = 'Le prénom doit contenir au moins 2 caractères';
      } else {
        delete this.errors.prenom;
      }
    },
    
    validateEmail() {
      const emailRegex = /^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\.[A-Za-z]{2,})$/;
      
      if (!this.email.trim()) {
        this.errors.email = 'L\'email est requis';
      } else if (!emailRegex.test(this.email.trim())) {
        this.errors.email = 'Format d\'email invalide';
      } else {
        delete this.errors.email;
      }
    },
    
    validateAdresse() {
      if (!this.adresse.trim()) {
        this.errors.adresse = 'L\'adresse est requise';
      } else if (this.adresse.trim().length < 5) {
        this.errors.adresse = 'L\'adresse doit contenir au moins 5 caractères';
      } else {
        delete this.errors.adresse;
      }
    },
    
    validateCodePostal() {
      const codePostalRegex = /^\d{4}$/;
      
      if (!this.codePostal.trim()) {
        this.errors.codePostal = 'Le code postal est requis';
      } else if (!codePostalRegex.test(this.codePostal.trim())) {
        this.errors.codePostal = 'Le code postal doit contenir 4 chiffres';
      } else {
        delete this.errors.codePostal;
      }
    },
    
    validateVille() {
      if (!this.ville.trim()) {
        this.errors.ville = 'La ville est requise';
      } else if (this.ville.trim().length < 2) {
        this.errors.ville = 'La ville doit contenir au moins 2 caractères';
      } else {
        delete this.errors.ville;
      }
    },
    
    validatePassword() {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/;
      
      if (!this.password) {
        this.errors.password = 'Le mot de passe est requis';
      } else if (this.password.length < 8) {
        this.errors.password = 'Le mot de passe doit contenir au moins 8 caractères';
      } else if (!passwordRegex.test(this.password)) {
        this.errors.password = 'Le mot de passe doit contenir au moins une majuscule, une minuscule et un chiffre';
      } else {
        delete this.errors.password;
      }
      
      // Re-valider la confirmation si elle existe
      if (this.confirmPassword) {
        this.validateConfirmPassword();
      }
    },
    
    validateConfirmPassword() {
      if (!this.confirmPassword) {
        this.errors.confirmPassword = 'La confirmation du mot de passe est requise';
      } else if (this.password !== this.confirmPassword) {
        this.errors.confirmPassword = 'Les mots de passe ne correspondent pas';
      } else {
        delete this.errors.confirmPassword;
      }
    },
    
    clearError(field) {
      delete this.errors[field];
      this.generalError = '';
    },
    
    validateAllFields() {
      this.validateNom();
      this.validatePrenom();
      this.validateEmail();
      this.validateAdresse();
      this.validateCodePostal();
      this.validateVille();
      this.validatePassword();
      this.validateConfirmPassword();
    },
    
    async handleRegister() {
      // Valider tous les champs avant soumission
      this.validateAllFields();
      
      if (Object.keys(this.errors).length > 0) {
        this.generalError = 'Veuillez corriger les erreurs avant de continuer';
        return;
      }

      this.loading = true;
      this.generalError = '';
      this.successMessage = '';

      try {
        await authService.register(
          this.nom.trim(),
          this.prenom.trim(),
          this.email.trim(),
          this.adresse.trim(),
          this.codePostal.trim(),
          this.ville.trim(),
          this.password
        );
        
        this.successMessage = 'Inscription réussie ! Connexion automatique...';
        
        // Émettre un événement pour notifier la navbar
        window.dispatchEvent(new CustomEvent('user-authenticated'));
        
        // Petit délai pour montrer le message de succès puis rediriger vers l'accueil
        setTimeout(() => {
          this.$router.push('/');
        }, 2000);
      } catch (err) {
        console.error('Erreur d\'inscription:', err);
        
        // Gestion spécifique des erreurs
        if (err.response?.status === 409) {
          this.generalError = 'Un compte existe déjà avec cet email';
        } else if (err.response?.status === 422) {
          this.generalError = 'Données invalides. Vérifiez vos informations';
        } else if (err.response?.data?.message) {
          this.generalError = err.response.data.message;
        } else if (err.message) {
          this.generalError = err.message;
        } else {
          this.generalError = 'Erreur lors de l\'inscription. Veuillez réessayer';
        }
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.auth-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
  background-color: #f5f5f5;
  padding: 20px;
}

.auth-card {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 500px;
}

.auth-card h2 {
  text-align: center;
  color: #333;
  margin-bottom: 1.5rem;
}

.auth-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group label {
  color: #555;
  font-size: 0.9rem;
}

.form-group input {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #42b983;
}

/* Styles pour la validation des champs */
.form-group input.error {
  border-color: #dc3545;
  background-color: #fff5f5;
}

.form-group input.valid {
  border-color: #28a745;
  background-color: #f8fff9;
}

.field-error {
  color: #dc3545;
  font-size: 0.8rem;
  margin-top: 0.25rem;
}

/* Indicateur de force du mot de passe */
.password-strength {
  margin-top: 0.5rem;
}

.strength-bar {
  height: 4px;
  background-color: #e0e0e0;
  border-radius: 2px;
  overflow: hidden;
  margin-bottom: 0.25rem;
}

.strength-fill {
  height: 100%;
  transition: width 0.3s ease;
}

.strength-bar.very-weak .strength-fill { background-color: #ff4444; }
.strength-bar.weak .strength-fill { background-color: #ff8800; }
.strength-bar.medium .strength-fill { background-color: #ffbb33; }
.strength-bar.strong .strength-fill { background-color: #00C851; }
.strength-bar.very-strong .strength-fill { background-color: #007E33; }

.strength-text {
  font-size: 0.8rem;
  color: #666;
}

.auth-button {
  background-color: #42b983;
  color: white;
  padding: 0.75rem;
  border: none;
  border-radius: 4px;
  font-size: 1rem;
  cursor: pointer;
  transition: background-color 0.3s ease;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.auth-button:hover:not(:disabled) {
  background-color: #3aa876;
}

.auth-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.auth-button.loading {
  background-color: #3aa876;
}

/* Spinner pour le bouton de chargement */
.spinner {
  width: 16px;
  height: 16px;
  border: 2px solid #ffffff30;
  border-top: 2px solid #ffffff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  color: #dc3545;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  padding: 0.75rem;
  border-radius: 4px;
  font-size: 0.9rem;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.success-message {
  color: #155724;
  background-color: #d4edda;
  border: 1px solid #c3e6cb;
  padding: 0.75rem;
  border-radius: 4px;
  font-size: 0.9rem;
  text-align: center;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.error-icon, .success-icon {
  font-size: 1.1rem;
}

.auth-switch {
  text-align: center;
  margin-top: 1rem;
  color: #666;
}

.switch-link {
  color: #42b983;
  text-decoration: none;
  font-weight: 500;
}

.switch-link:hover {
  text-decoration: underline;
}

@media (max-width: 600px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style> 