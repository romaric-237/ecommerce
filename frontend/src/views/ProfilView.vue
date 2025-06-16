<template>
  <div class="profile-container">
    <h2>Mon profil</h2>

    <form @submit.prevent="updateProfile">
      <div>
        <label>Prénom :</label>
        <input v-model="prenom" type="text" required />
      </div>
      <div>
        <label>Nom :</label>
        <input v-model="nom" type="text" required />
      </div>
      <div>
        <label>Email :</label>
        <input v-model="email" type="email" disabled />
      </div>
      <button type="submit">Mettre à jour</button>
    </form>

    <p v-if="message" :class="{ success: success, error: !success }">{{ message }}</p>
  </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore';
import { onMounted, ref } from 'vue';

export default {
  name: 'ProfilView',
  setup() {
    const userStore = useUserStore();
    const prenom = ref('');
    const nom = ref('');
    const email = ref('');
    const message = ref('');
    const success = ref(false);

    onMounted(async () => {
      await userStore.fetchUserProfile();
      if (userStore.user) {
        prenom.value = userStore.user.prenom;
        nom.value = userStore.user.nom;
        email.value = userStore.user.email;
      }
    });

    const updateProfile = async () => {
      const updatedUser = {
        prenom: prenom.value,
        nom: nom.value,
      };

      const result = await userStore.updateUser(updatedUser);
      message.value = result.message;
      success.value = result.success;
    };

    return {
      prenom,
      nom,
      email,
      message,
      success,
      updateProfile
    };
  }
};
</script>

<style scoped>
.profile-container {
  max-width: 500px;
  margin: 30px auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 10px;
}

input {
  width: 100%;
  padding: 8px;
  margin: 6px 0 12px 0;
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
