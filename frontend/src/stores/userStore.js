import { defineStore } from 'pinia';
import axios from 'axios';

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('jwt') || null,
    user: null,
  }),

  actions: {
    setToken(token) {
      this.token = token;
      localStorage.setItem('jwt', token);
    },

    async fetchUserProfile() {
      try {
        const response = await axios.get('http://localhost:8080/api/users/me', {
          headers: {
            Authorization: `Bearer ${this.token}`,
          },
        });
        this.user = response.data;
      } catch (error) {
        console.error('Erreur lors de la récupération du profil', error);
      }
    },

    async updateUser(updatedUser) {
      try {
        const response = await axios.put(
          `http://localhost:8080/api/users/${this.user.id}`,
          updatedUser,
          {
            headers: {
              Authorization: `Bearer ${this.token}`,
            },
          }
        );
        this.user = response.data;
        return { success: true, message: 'Profil mis à jour avec succès.' };
      } catch (error) {
        console.error('Erreur de mise à jour du profil :', error);
        return { success: false, message: 'La mise à jour a échoué.' };
      }
    },

    logout() {
      this.token = null;
      this.user = null;
      localStorage.removeItem('jwt');
    }
  }
});
