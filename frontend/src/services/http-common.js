import axios from 'axios';
import * as Qs from 'qs';

// Configuration de base pour l'API
const API_BASE_URL =  'http://localhost:8080';

// Clés pour le stockage des tokens
const TOKEN_KEY = 'accessToken';
const REFRESH_TOKEN_KEY = 'refreshToken';

// Création de l'instance axios
export const EcommerceApi = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 10000, // Timeout de 10 secondes
  paramsSerializer: params => Qs.stringify(params, { arrayFormat: 'repeat', skipNulls: true })
});

// Fonctions utilitaires pour la gestion des tokens
export const TokenUtils = {
  getAccessToken() {
    return sessionStorage.getItem(TOKEN_KEY);
  },

  getRefreshToken() {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
  },

  setTokens(accessToken, refreshToken) {
    console.log('=== TOKENUTILS.SETTOKENS ===')
    console.log('Access token reçu:', accessToken ? 'PRÉSENT' : 'ABSENT')
    console.log('Refresh token reçu:', refreshToken ? 'PRÉSENT' : 'ABSENT')
    
    if (accessToken) {
      console.log('Stockage accessToken dans sessionStorage...')
      sessionStorage.setItem(TOKEN_KEY, accessToken);
      console.log('AccessToken stocké avec succès')
    }
    if (refreshToken) {
      console.log('Stockage refreshToken dans localStorage...')
      localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
      console.log('RefreshToken stocké avec succès')
    }
    
    console.log('=== VÉRIFICATION APRÈS STOCKAGE ===')
    console.log('AccessToken en sessionStorage:', sessionStorage.getItem(TOKEN_KEY) ? 'PRÉSENT' : 'ABSENT')
    console.log('RefreshToken en localStorage:', localStorage.getItem(REFRESH_TOKEN_KEY) ? 'PRÉSENT' : 'ABSENT')
  },

  clearTokens() {
    sessionStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    sessionStorage.removeItem('user');
  },

  isTokenExpired(token) {
    if (!token) return true;
    
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const currentTime = Date.now() / 1000;
      return payload.exp < currentTime;
    } catch (error) {
      console.error('Erreur lors de la vérification du token:', error);
      return true;
    }
  },

  async refreshToken() {
    const refreshToken = this.getRefreshToken();
    if (!refreshToken || this.isTokenExpired(refreshToken)) {
      throw new Error('Refresh token invalide ou expiré');
    }

    try {
      // Utiliser une instance axios sans intercepteurs pour éviter les boucles infinies
      const response = await axios.post(`${API_BASE_URL}/api/auth/refresh`, {
        refreshToken
      });
      
      if (response.data.accessToken) {
        this.setTokens(response.data.accessToken, response.data.refreshToken);
        return response.data.accessToken;
      }
      
      throw new Error('Pas de nouveau token reçu');
    } catch (error) {
      console.error('Erreur lors du renouvellement du token:', error);
      this.clearTokens();
      throw error;
    }
  }
};

// Configuration des intercepteurs
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  
  failedQueue = [];
};

// Intercepteur de requête pour ajouter le token
EcommerceApi.interceptors.request.use(
  (config) => {
    const token = TokenUtils.getAccessToken();
    
    // Ajouter le token s'il existe et n'est pas expiré
    if (token && !TokenUtils.isTokenExpired(token)) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // Log pour debug (à retirer en production)
    console.log(`[HTTP] ${config.method.toUpperCase()} ${config.url}`);
    
    return config;
  },
  (error) => {
    console.error('[HTTP] Erreur dans l\'intercepteur de requête:', error);
    return Promise.reject(error);
  }
);

// Intercepteur de réponse pour gérer le renouvellement automatique
EcommerceApi.interceptors.response.use(
  (response) => {
    // Log pour debug (à retirer en production)
    console.log(`[HTTP] ${response.status} ${response.config.method.toUpperCase()} ${response.config.url}`);
    return response;
  },
  async (error) => {
    const originalRequest = error.config;
    
    console.error(`[HTTP] ${error.response?.status || 'NETWORK'} ${originalRequest?.method?.toUpperCase() || 'UNKNOWN'} ${originalRequest?.url || 'UNKNOWN'}`);
    
    // Si c'est une erreur 401 et que la requête n'a pas encore été retentée
    if (error.response?.status === 401 && !originalRequest._retry) {
      // Si on est déjà en train de rafraîchir le token
      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(token => {
          originalRequest.headers.Authorization = `Bearer ${token}`;
          return EcommerceApi(originalRequest);
        }).catch(err => {
          return Promise.reject(err);
        });
      }
      
      originalRequest._retry = true;
      isRefreshing = true;
      
      try {
        const newToken = await TokenUtils.refreshToken();
        console.log(newToken);
        // Traiter la queue des requêtes en attente
        processQueue(null, newToken);
        
        // Réessayer la requête originale avec le nouveau token
        originalRequest.headers.Authorization = `Bearer ${newToken}`;
        return EcommerceApi(originalRequest);
      } catch (refreshError) {
        // Échec du renouvellement
        processQueue(refreshError, null);
        TokenUtils.clearTokens();
        
        // Rediriger vers la page de connexion
        if (typeof window !== 'undefined' && window.location) {
         // window.location.href = '/login';
        }
        
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }
    
    return Promise.reject(error);
  }
);

// Instance par défaut
export default EcommerceApi;

