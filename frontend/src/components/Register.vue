<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-white">
    <div class="container">
      <div class="row justify-content-center align-items-center min-vh-100">
        <div class="col-md-6 col-lg-5">
          <div class="card shadow-lg border-0 rounded-lg">
            <div class="card-body p-5">
              <!-- En-tête -->
              <div class="text-center mb-4">
                <h2 class="fw-bold text-primary mb-2">
                  Créer un compte
                </h2>
                <p class="text-muted">
                  Ou
                  <router-link to="/login" class="text-decoration-none text-primary fw-medium">
                    connectez-vous
                  </router-link>
                </p>
              </div>

              <!-- Formulaire -->
              <form @submit.prevent="handleRegister" class="needs-validation" novalidate>
                <!-- Nom -->
                <div class="form-floating mb-3">
                  <input
                    type="text"
                    class="form-control"
                    id="lastName"
                    v-model="form.lastName"
                    placeholder="Nom"
                    required
                  />
                  <label for="lastName">Nom</label>
                </div>

                <!-- Prénom -->
                <div class="form-floating mb-3">
                  <input
                    type="text"
                    class="form-control"
                    id="firstName"
                    v-model="form.firstName"
                    placeholder="Prénom"
                    required
                  />
                  <label for="firstName">Prénom</label>
                </div>

                <!-- Email -->
                <div class="form-floating mb-3">
                  <input
                    type="email"
                    class="form-control"
                    id="email"
                    v-model="form.email"
                    placeholder="exemple@email.com"
                    required
                  />
                  <label for="email">Adresse email</label>
                </div>

                <!-- Téléphone -->
                <div class="form-floating mb-3">
                  <input
                    type="tel"
                    class="form-control"
                    id="phone"
                    v-model="form.phone"
                    placeholder="Téléphone"
                    required
                  />
                  <label for="phone">Téléphone</label>
                </div>

                <!-- Mot de passe -->
                <div class="form-floating mb-3">
                  <div class="input-group">
                    <input
                      :type="showPassword ? 'text' : 'password'"
                      class="form-control"
                      id="password"
                      v-model="form.password"
                      placeholder="Mot de passe"
                      required
                    />
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      @click="togglePassword"
                    >
                      <i :class="showPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                    </button>
                  </div>
                </div>

                <!-- Confirmation du mot de passe -->
                <div class="form-floating mb-4">
                  <div class="input-group">
                    <input
                      :type="showConfirmPassword ? 'text' : 'password'"
                      class="form-control"
                      id="confirmPassword"
                      v-model="form.confirmPassword"
                      placeholder="Confirmer le mot de passe"
                      required
                    />
                    <button
                      class="btn btn-outline-secondary"
                      type="button"
                      @click="toggleConfirmPassword"
                    >
                      <i :class="showConfirmPassword ? 'bi bi-eye-slash' : 'bi bi-eye'"></i>
                    </button>
                  </div>
                </div>

                <!-- Message d'erreur -->
                <div v-if="error" class="alert alert-danger d-flex align-items-center mb-4" role="alert">
                  <i class="bi bi-exclamation-triangle-fill me-2"></i>
                  <div>{{ error }}</div>
                </div>

                <!-- Bouton d'inscription -->
                <button
                  type="submit"
                  class="btn btn-primary w-100 py-2"
                  :disabled="loading"
                >
                  <span v-if="loading" class="spinner-border spinner-border-sm me-2" role="status"></span>
                  {{ loading ? 'Inscription en cours...' : 'S\'inscrire' }}
                </button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';

export default {
  name: 'Register',
  data() {
    return {
      form: {
        firstName: '',
        lastName: '',
        email: '',
        phone: '',
        password: '',
        confirmPassword: ''
      },
      showPassword: false,
      showConfirmPassword: false,
      loading: false,
      error: null
    };
  },
  methods: {
    togglePassword() {
      this.showPassword = !this.showPassword;
    },
    toggleConfirmPassword() {
      this.showConfirmPassword = !this.showConfirmPassword;
    },
    async handleRegister() {
      if (this.form.password !== this.form.confirmPassword) {
        this.error = 'Les mots de passe ne correspondent pas';
        return;
      }

      this.loading = true;
      this.error = null;
      
      try {
        const response = await axios.post('http://localhost:8080/api/auth/register', {
          firstName: this.form.firstName,
          lastName: this.form.lastName,
          email: this.form.email,
          phone: this.form.phone,
          password: this.form.password
        });
        
        localStorage.setItem('token', response.data.token);
        this.$router.push('/');
      } catch (error) {
        this.error = error.response?.data?.message || 'Une erreur est survenue lors de l\'inscription';
      } finally {
        this.loading = false;
      }
    }
  }
};
</script>

<style scoped>
.card {
  backdrop-filter: blur(10px);
  background-color: rgba(255, 255, 255, 0.9);
}

.form-control:focus {
  border-color: #0d6efd;
  box-shadow: 0 0 0 0.25rem rgba(13, 110, 253, 0.25);
}

.btn-primary {
  background-color: #0d6efd;
  border-color: #0d6efd;
  transition: all 0.3s ease;
}

.btn-primary:hover {
  background-color: #0b5ed7;
  border-color: #0a58ca;
  transform: translateY(-1px);
}

.form-floating > .form-control {
  height: calc(3.5rem + 2px);
  line-height: 1.25;
}

.form-floating > label {
  padding: 1rem 0.75rem;
}

.input-group .btn {
  z-index: 0;
}
</style> 