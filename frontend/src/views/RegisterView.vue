<template>
  <div class="register-container">
    <h2>Inscription</h2>
    <form @submit.prevent="register">
      <div>
        <label>Nom :</label>
        <input v-model="form.nom" type="text" required />
      </div>
      <div>
        <label>Prénom :</label>
        <input v-model="form.prenom" type="text" required />
      </div>
      <div>
        <label>Email :</label>
        <input v-model="form.email" type="email" required />
      </div>
      <div>
        <label>Mot de passe :</label>
        <input v-model="form.motDePasse" type="password" required />
      </div>
      <button type="submit">S'inscrire</button>
    </form>

    <p v-if="message">{{ message }}</p>
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: "RegisterView",
  data() {
    return {
      form: {
        nom: "",
        prenom: "",
        email: "",
        motDePasse: ""
      },
      message: ""
    };
  },
  methods: {
    async register() {
      try {
        const response = await axios.post("http://localhost:8080/api/auth/register", this.form);
        this.message = response.data;
      } catch (error) {
        this.message = error.response?.data || "Erreur lors de l'inscription.";
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
}
form > div {
  margin-bottom: 10px;
}
</style>
