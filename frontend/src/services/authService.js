import {EcommerceApi, TokenUtils} from './http-common.js';


const API_URL = 'http://localhost:9999/api/auth/';
const USER_KEY = 'user';

class AuthService {
  // Les intercepteurs sont maintenant gérés dans http-common.js

  async login(email, password) {
    try {
      const response = await EcommerceApi.post('auth/login', {
        email: email.toLowerCase().trim(),
        password
      });
      
      console.log('=== RÉPONSE LOGIN ===')
      console.log('Réponse complète:', response.data)
      console.log('Access token présent:', !!response.data.accessToken)
      console.log('Refresh token présent:', !!response.data.refreshToken)
      
      if (response.data.accessToken) {
        console.log('=== STOCKAGE DES TOKENS ===')
        console.log('Avant stockage - accessToken:', response.data.accessToken)
        console.log('Avant stockage - refreshToken:', response.data.refreshToken)
        
        TokenUtils.setTokens(response.data.accessToken, response.data.refreshToken);
        
        console.log('=== APRÈS STOCKAGE ===')
        console.log('Token stocké dans sessionStorage:', sessionStorage.getItem('accessToken'))
        console.log('Refresh token stocké dans localStorage:', localStorage.getItem('refreshToken'))
        
        this.setUser(response.data.user);
        
        // Programmer le renouvellement automatique
        this.scheduleTokenRefresh(response.data.expiresIn);
        
        // Émettre un événement pour notifier la connexion
        window.dispatchEvent(new CustomEvent('user-authenticated', {
          detail: { user: response.data.user }
        }));
      }
      
      return response.data;
    } catch (error) {
      console.error('Erreur de connexion:', error.response?.data || error.message);
      throw new Error(error.response?.data?.message || 'Erreur de connexion');
    }
  }

  async register(nom, prenom, email, adresse, codePostal, ville, password) {
    try {
      const response = await EcommerceApi.post('/auth/register', {
        nom: nom.trim(),
        prenom: prenom.trim(),
        email: email.toLowerCase().trim(),
        adresse: adresse.trim(),
        codePostal: codePostal.trim(),
        ville: ville.trim(),
        password
      });
      
      if (response.data.accessToken) {
        TokenUtils.setTokens(response.data.accessToken, response.data.refreshToken);
        this.setUser(response.data.user);
        this.scheduleTokenRefresh(response.data.expiresIn);
      }
      
      return response.data;
    } catch (error) {
      console.error('Erreur d\'enregistrement:', error.response?.data || error.message);
      throw error; // Laisser l'erreur originale pour que le composant puisse la traiter
    }
  }

  async refreshAccessToken() {
    try {
      const newToken = await TokenUtils.refreshToken();
      // Programmer le renouvellement pour le nouveau token
      const payload = JSON.parse(atob(newToken.split('.')[1]));
      const expiresIn = payload.exp - Math.floor(Date.now() / 1000);
      this.scheduleTokenRefresh(expiresIn);
      return { accessToken: newToken };
    } catch (error) {
      console.error('Erreur lors du renouvellement:', error);
      this.logout();
      throw error;
    }
  }

  scheduleTokenRefresh(expiresInSeconds) {
    // Programmer le renouvellement 2 minutes avant l'expiration
    const refreshTime = (expiresInSeconds - 120) * 1000;
    
    if (refreshTime > 0) {
      setTimeout(() => {
        if (this.isAuthenticated()) {
          this.refreshAccessToken().catch(() => {
            this.logout();
          });
        }
      }, refreshTime);
    }
  }

  async logout() {
    try {
      const token = TokenUtils.getAccessToken();
      if (token) {
        return await EcommerceApi.post('/auth/logout', {}, {
          headers: {Authorization: `Bearer ${token}`}
        });
      }
    } catch (error) {
      console.error('Erreur lors de la déconnexion:', error);
    } finally {
      TokenUtils.clearTokens();
      this.clearUser();
      
      // Émettre un événement pour notifier la déconnexion
      window.dispatchEvent(new CustomEvent('user-logged-out'));
      
      window.location.href = '/login';
    }
  }

  setUser(user) {
    if (user) {
      console.log('Utilisateur stocké:', user)
      sessionStorage.setItem(USER_KEY, JSON.stringify(user))
    }
  }

  getCurrentUser() {
    const user = sessionStorage.getItem(USER_KEY)
    const parsedUser = user ? JSON.parse(user) : null
    console.log('Utilisateur récupéré:', parsedUser)
    return parsedUser
  }

  clearUser() {
    sessionStorage.removeItem(USER_KEY);
  }

  isAuthenticated() {
    const accessToken = TokenUtils.getAccessToken();
    const refreshToken = TokenUtils.getRefreshToken();
    
    if (accessToken && !TokenUtils.isTokenExpired(accessToken)) {
      return true;
    }
    
    return refreshToken && !TokenUtils.isTokenExpired(refreshToken);
  }


  async validateToken() {
    try {
      const response = await EcommerceApi.get('/auth/validate');
      return response.data.valid;
    } catch (error) {
      return false;
    }
  }
}

export default new AuthService();
