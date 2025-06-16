<template>
  <div class="login-container">
    <h2>Connexion</h2>
    <form @submit.prevent="loginUser">
      <div>
        <label>Email :</label>
        <input type="email" v-model="email" required />
      </div>
      <div>
        <label>Mot de passe :</label>
        <input type="password" v-model="motDePasse" required />
      </div>
      <button type="submit">Se connecter</button>
    </form>
    <p v-if="message" :class="{ 'success': success, 'error': !success }">{{ message }}</p>
  </div>
</template>

<script>
import axios from 'axios';
import { useUserStore } from '@/stores/userStore';

export default {
  name: 'LoginView',
  data() {
    return {
      email: '',
      motDePasse: '',
      message: '',
      success: false,
    };
  },
  computed: {
    userStore() {
      return useUserStore();
    }
  },
  methods: {
    async loginUser() {
      try {
        const response = await axios.post('http://localhost:8080/api/auth/login', {
          email: this.email,
          motDePasse: this.motDePasse,
        });

        this.message = response.data.message;
        this.success = true;

        // Stockage du token via Pinia
        this.userStore.login(response.data.token);

        this.$router.push('/products');
      } catch (error) {
        console.log(error.response);
        this.message = error.response?.data || 'Erreur lors de la connexion.';
        this.success = false;
      }
    },
  },
};

</script>

<style scoped>
.login-container {
  max-width: 400px;
  margin: 30px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}
input {
  width: 100%;
  padding: 8px;
  margin: 6px 0;
}
button {
  background-color: #42b983;
  color: white;
  padding: 10px;
  border: none;
  cursor: pointer;
}
.success {
  color: green;
  margin-top: 10px;
}
.error {
  color: red;
  margin-top: 10px;
}
</style>
