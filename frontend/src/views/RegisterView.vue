<template>
  <div class="register-container">
    <h2>Inscription</h2>
    
    <form @submit.prevent="submitForm" class="form-register">
      <label>Nom :</label>
      <input v-model="form.nom" required />

      <label>Prénom :</label>
      <input v-model="form.prenom" required />

      <label>Email :</label>
      <input type="email" v-model="form.email" required />

      <label>Mot de passe :</label>
      <input type="password" v-model="form.motDePasse" required />

      <button type="submit">S'inscrire</button>
    </form>

    <p v-if="successMessage" class="success-message">{{ successMessage }}</p>
    <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "RegisterView",
  data() {
    return {
      form: {
        nom: "",
        prenom: "",
        email: "",
        motDePasse: "",
      },
      successMessage: "",
      errorMessage: ""
    };
  },
  methods: {
    async submitForm() {
      try {
        const response = await axios.post("http://localhost:8080/api/auth/register", this.form);
        this.successMessage = response.data;
        this.errorMessage = "";

        // Redirection après 2 secondes
        setTimeout(() => {
          this.$router.push("/products");
        }, 2000);

      } catch (error) {
        this.errorMessage = "Erreur lors de l'inscription.";
        this.successMessage = "";
      }
    }
  }
};
</script>

<style scoped>
.register-container {
  max-width: 400px;
  margin: auto;
  padding: 20px;
  text-align: left;
}

.form-register label {
  display: block;
  margin-top: 12px;
  font-weight: bold;
}

.form-register input {
  width: 100%;
  padding: 8px;
  margin-top: 4px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  margin-top: 20px;
  padding: 10px 20px;
  background-color: #42b983;
  border: none;
  color: white;
  cursor: pointer;
  border-radius: 5px;
  transition: background-color 0.3s ease;
}

button:hover {
  background-color: #36996b;
}

.success-message {
  color: #2ecc71;
  margin-top: 15px;
  font-weight: bold;
  animation: fadeIn 0.8s ease;
}

.error-message {
  color: #e74c3c;
  margin-top: 15px;
  font-weight: bold;
  animation: shake 0.4s;
}

/* Animations */
@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

@keyframes shake {
  0% { transform: translateX(0); }
  25% { transform: translateX(-4px); }
  50% { transform: translateX(4px); }
  75% { transform: translateX(-4px); }
  100% { transform: translateX(0); }
}
</style>
