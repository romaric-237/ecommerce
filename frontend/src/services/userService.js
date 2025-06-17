import EcommerceApi from './http-common';

class UserService {
  async updateUser(userId, userData) {
    try {
      const response = await EcommerceApi.put(`/users/update/${userId}`, userData);
      return response.data.user;
    } catch (error) {
      console.error('Erreur lors de la mise à jour du profil:', error.response?.data || error.message);
      throw error;
    }
  }

  async getUserById(userId) {
    try {
      const response = await EcommerceApi.get(`/users/${userId}`);
      return response.data.user;
    } catch (error) {
      console.error('Erreur lors de la récupération du profil:', error.response?.data || error.message);
      throw error;
    }
  }
}

export default new UserService(); 