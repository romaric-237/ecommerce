<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>Connexion</h2>
      <form @submit.prevent="handleLogin" class="auth-form">
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
          {{ loading ? 'Connexion...' : 'Se connecter' }}
        </button>
        <p class="auth-switch">
          Pas encore de compte ? 
          <router-link to="/register" class="switch-link">S'inscrire</router-link>
        </p>
      </form>
    </div>
  </div>
</template>

<script>
import authService from '@/services/authService';

export default {
  name: 'Login',
  data() {
    return {
      email: '',
      password: '',
      errors: {},
      generalError: '',
      successMessage: '',
      loading: false
    };
  },
  computed: {
    isFormValid() {
      return this.email.trim() && 
             this.password && 
             Object.keys(this.errors).length === 0;
    }
  },
  methods: {
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
    
    validatePassword() {
      if (!this.password) {
        this.errors.password = 'Le mot de passe est requis';
      } else if (this.password.length < 6) {
        this.errors.password = 'Le mot de passe doit contenir au moins 6 caractères';
      } else {
        delete this.errors.password;
      }
    },
    
    clearError(field) {
      delete this.errors[field];
      this.generalError = '';
    },
    
    async handleLogin() {
      // Valider tous les champs avant soumission
      this.validateEmail();
      this.validatePassword();
      
      if (Object.keys(this.errors).length > 0) {
        this.generalError = 'Veuillez corriger les erreurs avant de continuer';
        return;
      }

      this.loading = true;
      this.generalError = '';
      this.successMessage = '';

      try {
        await authService.login(this.email.trim(), this.password);
        this.successMessage = 'Connexion réussie ! Redirection...';
        
        // Émettre un événement pour notifier la navbar
        window.dispatchEvent(new CustomEvent('user-authenticated'));
        
        // Petit délai pour montrer le message de succès
        setTimeout(() => {
          this.$router.push('/');
        }, 1000);
      } catch (err) {
        console.error('Erreur de connexion:', err);
        
        // Gestion spécifique des erreurs
        if (err.response?.status === 401) {
          this.generalError = 'Email ou mot de passe incorrect';
        } else if (err.response?.status === 429) {
          this.generalError = 'Trop de tentatives. Veuillez réessayer plus tard';
        } else if (err.response?.data?.message) {
          this.generalError = err.response.data.message;
        } else if (err.message) {
          this.generalError = err.message;
        } else {
          this.generalError = 'Erreur de connexion. Veuillez réessayer';
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
  max-width: 400px;
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
</style> 