<template>
  <div class="profile-container">
    <div class="profile-card">
      <h2>Mon Profil</h2>
      <form @submit.prevent="handleUpdate" class="profile-form">
        <div class="form-group">
          <label for="nom">Nom</label>
          <input 
            type="text" 
            id="nom" 
            v-model="formData.nom" 
            :class="{ 'error': errors.nom, 'valid': formData.nom && !errors.nom }"
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
            v-model="formData.prenom" 
            :class="{ 'error': errors.prenom, 'valid': formData.prenom && !errors.prenom }"
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
            v-model="formData.email" 
            :class="{ 'error': errors.email, 'valid': formData.email && !errors.email }"
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
            v-model="formData.adresse" 
            :class="{ 'error': errors.adresse, 'valid': formData.adresse && !errors.adresse }"
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
              v-model="formData.codePostal" 
              :class="{ 'error': errors.codePostal, 'valid': formData.codePostal && !errors.codePostal }"
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
              v-model="formData.ville" 
              :class="{ 'error': errors.ville, 'valid': formData.ville && !errors.ville }"
              @blur="validateVille"
              @input="clearError('ville')"
              placeholder="Ville"
            >
            <span v-if="errors.ville" class="field-error">{{ errors.ville }}</span>
          </div>
        </div>

        <div class="form-group">
          <label for="password">Nouveau mot de passe (optionnel)</label>
          <input 
            type="password" 
            id="password" 
            v-model="formData.password" 
            :class="{ 'error': errors.password, 'valid': formData.password && !errors.password }"
            @blur="validatePassword"
            @input="clearError('password')"
            placeholder="Laissez vide pour ne pas changer"
          >
          <span v-if="errors.password" class="field-error">{{ errors.password }}</span>
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
          class="update-button" 
          :disabled="loading || !isFormValid"
          :class="{ 'loading': loading }"
        >
          <span v-if="loading" class="spinner"></span>
          {{ loading ? 'Mise à jour...' : 'Mettre à jour mon profil' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script>
import authService from '@/services/authService';
import userService from '@/services/userService';

export default {
  name: 'ProfileView',
  data() {
    return {
      formData: {
        nom: '',
        prenom: '',
        email: '',
        adresse: '',
        codePostal: '',
        ville: '',
        password: ''
      },
      errors: {},
      generalError: '',
      successMessage: '',
      loading: false
    };
  },
  computed: {
    isFormValid() {
      return this.formData.nom.trim() && 
             this.formData.prenom.trim() && 
             this.formData.email.trim() && 
             this.formData.adresse.trim() && 
             this.formData.codePostal.trim() && 
             this.formData.ville.trim() && 
             Object.keys(this.errors).length === 0;
    }
  },
  async created() {
    try {
      const user = authService.getCurrentUser();
      if (user) {
        this.formData = {
          nom: user.nom || '',
          prenom: user.prenom || '',
          email: user.email || '',
          adresse: user.adresse || '',
          codePostal: user.codePostal || '',
          ville: user.ville || '',
          password: ''
        };
      }
    } catch (error) {
      console.error('Erreur lors du chargement du profil:', error);
      this.generalError = 'Erreur lors du chargement du profil';
    }
  },
  methods: {
    validateNom() {
      if (!this.formData.nom.trim()) {
        this.errors.nom = 'Le nom est requis';
      } else if (this.formData.nom.trim().length < 2) {
        this.errors.nom = 'Le nom doit contenir au moins 2 caractères';
      } else {
        delete this.errors.nom;
      }
    },
    
    validatePrenom() {
      if (!this.formData.prenom.trim()) {
        this.errors.prenom = 'Le prénom est requis';
      } else if (this.formData.prenom.trim().length < 2) {
        this.errors.prenom = 'Le prénom doit contenir au moins 2 caractères';
      } else {
        delete this.errors.prenom;
      }
    },
    
    validateEmail() {
      const emailRegex = /^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\.[A-Za-z]{2,})$/;
      
      if (!this.formData.email.trim()) {
        this.errors.email = 'L\'email est requis';
      } else if (!emailRegex.test(this.formData.email.trim())) {
        this.errors.email = 'Format d\'email invalide';
      } else {
        delete this.errors.email;
      }
    },
    
    validateAdresse() {
      if (!this.formData.adresse.trim()) {
        this.errors.adresse = 'L\'adresse est requise';
      } else if (this.formData.adresse.trim().length < 5) {
        this.errors.adresse = 'L\'adresse doit contenir au moins 5 caractères';
      } else {
        delete this.errors.adresse;
      }
    },
    
    validateCodePostal() {
      const codePostalRegex = /^\d{4}$/;
      
      if (!this.formData.codePostal.trim()) {
        this.errors.codePostal = 'Le code postal est requis';
      } else if (!codePostalRegex.test(this.formData.codePostal.trim())) {
        this.errors.codePostal = 'Le code postal doit contenir 4 chiffres';
      } else {
        delete this.errors.codePostal;
      }
    },
    
    validateVille() {
      if (!this.formData.ville.trim()) {
        this.errors.ville = 'La ville est requise';
      } else if (this.formData.ville.trim().length < 2) {
        this.errors.ville = 'La ville doit contenir au moins 2 caractères';
      } else {
        delete this.errors.ville;
      }
    },
    
    validatePassword() {
      if (this.formData.password) {
        const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/;
        
        if (this.formData.password.length < 8) {
          this.errors.password = 'Le mot de passe doit contenir au moins 8 caractères';
        } else if (!passwordRegex.test(this.formData.password)) {
          this.errors.password = 'Le mot de passe doit contenir au moins une majuscule, une minuscule et un chiffre';
        } else {
          delete this.errors.password;
        }
      } else {
        delete this.errors.password;
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
      if (this.formData.password) {
        this.validatePassword();
      }
    },
    
    async handleUpdate() {
      this.validateAllFields();
      
      if (Object.keys(this.errors).length > 0) {
        this.generalError = 'Veuillez corriger les erreurs avant de continuer';
        return;
      }

      this.loading = true;
      this.generalError = '';
      this.successMessage = '';

      try {
        const user = authService.getCurrentUser();
        if (!user || !user.id) {
          throw new Error('Utilisateur non connecté');
        }

        const updatedUser = await userService.updateUser(user.id, this.formData);
        
        // Mettre à jour les informations de l'utilisateur dans le service d'authentification
        authService.setUser(updatedUser);
        
        this.successMessage = 'Profil mis à jour avec succès !';
        
        // Émettre un événement pour notifier la navbar
        window.dispatchEvent(new CustomEvent('user-authenticated'));
        
      } catch (err) {
        console.error('Erreur lors de la mise à jour du profil:', err);
        
        if (err.response?.status === 409) {
          this.generalError = 'Un compte existe déjà avec cet email';
        } else if (err.response?.status === 422) {
          this.generalError = 'Données invalides. Vérifiez vos informations';
        } else if (err.response?.data?.message) {
          this.generalError = err.response.data.message;
        } else if (err.message) {
          this.generalError = err.message;
        } else {
          this.generalError = 'Erreur lors de la mise à jour du profil. Veuillez réessayer';
        }
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.profile-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 64px);
  background-color: #f5f5f5;
  padding: 20px;
}

.profile-card {
  background: white;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 600px;
}

.profile-card h2 {
  text-align: center;
  color: #333;
  margin-bottom: 1.5rem;
}

.profile-form {
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

.update-button {
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

.update-button:hover:not(:disabled) {
  background-color: #3aa876;
}

.update-button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.update-button.loading {
  background-color: #3aa876;
}

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

@media (max-width: 600px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}
</style> 