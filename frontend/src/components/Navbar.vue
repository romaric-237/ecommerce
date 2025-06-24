<script>
import authService from '@/services/authService';
import { TokenUtils } from '@/services/http-common';
import Cart from '@/components/Cart.vue';

export default {
  name: "Navbar",
  components: {
    Cart
  },

  data() {
    return {
      activeTab: "produits",
      isAuthenticated: false,
      user: null,
      isUserMenuOpen: false
    };
  },
  created() {
    // Vérifier si l'utilisateur est connecté au chargement du composant
    this.checkAuth();
    // Fermer le menu si on clique en dehors
    document.addEventListener('click', this.handleClickOutside);
  },
  mounted() {
    // Écouter les événements d'authentification
    window.addEventListener('user-authenticated', this.onUserAuthenticated);
    window.addEventListener('user-logged-out', this.onUserLoggedOut);
  },
  beforeUnmount() {
    document.removeEventListener('click', this.handleClickOutside);
    window.removeEventListener('user-authenticated', this.onUserAuthenticated);
    window.removeEventListener('user-logged-out', this.onUserLoggedOut);
  },
  methods: {
    setActiveTab(tab) {
      this.activeTab = tab;
      if (tab === 'produits') {
        this.$router.push('/products');
      } else if (tab === 'categories') {
        this.$router.push('/categories');
      }
      this.$emit("tab-changed", tab);
    },
    checkAuth() {
      // Utiliser authService au lieu de localStorage direct
      this.isAuthenticated = authService.isAuthenticated();
      if (this.isAuthenticated) {
        this.user = authService.getCurrentUser();
      }
    },
    async logout() {
      try {
        await authService.logout();
        // authService.logout() redirige déjà vers /login
      } catch (error) {
        console.error('Erreur lors de la déconnexion:', error);
        // Forcer la déconnexion locale en cas d'erreur
        TokenUtils.clearTokens();
        this.isAuthenticated = false;
        this.user = null;
        this.isUserMenuOpen = false;
        this.$router.push('/login');
      }
    },
    toggleUserMenu(event) {
      event.stopPropagation();
      this.isUserMenuOpen = !this.isUserMenuOpen;
    },
    handleClickOutside(event) {
      if (!event.target.closest('.user-menu')) {
        this.isUserMenuOpen = false;
      }
    },
    getAvatarInitials(user) {
      if (!user) return '?';
      
      const prenom = user.prenom || '';
      const nom = user.nom || '';
      
      const firstInitial = prenom.charAt(0).toUpperCase();
      const lastInitial = nom.charAt(0).toUpperCase();
      
      return `${firstInitial}${lastInitial}` || user.email?.charAt(0).toUpperCase() || '?';
    },
    getUserDisplayName(user) {
      if (!user) return 'Utilisateur';
      
      if (user.prenom && user.nom) {
        return `${user.prenom} ${user.nom}`;
      } else if (user.prenom) {
        return user.prenom;
      } else if (user.nom) {
        return user.nom;
      } else if (user.email) {
        return user.email.split('@')[0];
      }
      
      return 'Utilisateur';
    },
    onUserAuthenticated() {
      // Mettre à jour l'état d'authentification quand un utilisateur se connecte
      this.checkAuth();
    },
    onUserLoggedOut() {
      // Mettre à jour l'état quand un utilisateur se déconnecte
      this.isAuthenticated = false;
      this.user = null;
      this.isUserMenuOpen = false;
    }
  },
  watch: {
    '$route.path': {
      immediate: true,
      handler(newPath) {
        if (newPath.includes('/products')) {
          this.activeTab = 'produits';
        } else if (newPath.includes('/categories')) {
          this.activeTab = 'categories';
        } else {
          this.activeTab = 'produits';
        }
      }
    }
  }
};
</script>

<template>
  <nav class="app-navbar">
    <div class="navbar-content">
      <router-link to="/" class="navbar-brand-custom">Ecommerce</router-link>

      <div class="navbar-links">
        <ul class="nav-list">
          <li class="nav-item-custom">
            <router-link to="/products"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'produits' }"
                         @click="setActiveTab('produits')">Produits
            </router-link>
          </li>
          <li class="nav-item-custom">
            <router-link to="/categories"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'categories' }"
                         @click="setActiveTab('categories')">Catégories
            </router-link>
          </li>
        </ul>
      </div>
      
      <!-- Composant Panier -->
      <Cart />
      
      <div class="navbar-auth">
        <template v-if="isAuthenticated">
          <div class="user-menu">
            <button class="user-menu-button" @click="toggleUserMenu">
              <div class="user-avatar-container">
                <div class="user-avatar-circle">
                  {{ getAvatarInitials(user) }}
                </div>
              </div>
              <span class="user-name">{{ getUserDisplayName(user) }}</span>
              <div class="chevron-icon" :class="{ 'rotate': isUserMenuOpen }">
                <svg width="12" height="8" viewBox="0 0 12 8" fill="currentColor">
                  <path d="M1 1l5 5 5-5" stroke="currentColor" stroke-width="2" fill="none" stroke-linecap="round" stroke-linejoin="round"/>
                </svg>
              </div>
            </button>
            <div class="user-dropdown" v-if="isUserMenuOpen">
              <router-link to="/profile" class="dropdown-item" @click="isUserMenuOpen = false">
                <span class="dropdown-icon">👤</span>
                Mon Profil
              </router-link>
              <router-link to="/orders" class="dropdown-item" @click="isUserMenuOpen = false">
                <span class="dropdown-icon">🛍️</span>
                Mes Commandes
              </router-link>
              <div class="dropdown-divider"></div>
              <button @click="logout" class="dropdown-item logout">
                <span class="dropdown-icon">🚪</span>
                Déconnexion
              </button>
            </div>
          </div>
        </template>
        <template v-else>
          <ul class="nav-list">
            <li class="nav-item-custom" v-if="!$route.path.includes('/login')">
              <router-link to="/login" class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'login' }"
              >Connexion</router-link>
            </li>
            <li class="nav-item-custom" v-if="!$route.path.includes('/register')">
              <router-link to="/register" class="nav-link-custom register-link"
                         :class="{ 'active-link': activeTab === 'register' }">Inscription</router-link>
            </li>
          </ul>
        </template>
      </div>
      

    </div>
  </nav>
</template>

<style scoped>
/* Variables CSS ultra-modernes */
:root {
  /* Palette principale */
  --navbar-primary: #B85E5E26;
  --navbar-secondary: #8FAF1326;
  --navbar-tertiary: #2797B126;
  
  /* Accents et highlights */
  --navbar-accent: #00d4aa;
  --navbar-accent-hover: #00b894;
  --navbar-accent-glow: rgba(0, 212, 170, 0.3);
  
  /* Texte et contenu */
  --navbar-text: #1c0c0c;
  --navbar-text-secondary: #e2e8f0;
  --navbar-text-muted: #a0aec0;
  --navbar-text-disabled: #718096;
  
  /* Glassmorphism */
  --navbar-glass: rgba(255, 255, 255, 0.08);
  --navbar-glass-border: rgba(207, 124, 124, 0.12);
  --navbar-glass-hover: rgba(255, 255, 255, 0.15);
  
  /* Shadows premium */
  --navbar-shadow-sm: 0 2px 8px rgba(0, 0, 0, 0.12);
  --navbar-shadow-md: 0 8px 32px rgba(0, 0, 0, 0.18);
  --navbar-shadow-lg: 0 16px 48px rgba(0, 0, 0, 0.25);
  --navbar-shadow-glow: 0 0 20px var(--navbar-accent-glow);
  
  /* Borders et separateurs */
  --navbar-border: rgba(255, 255, 255, 0.1);
  --navbar-border-hover: rgba(255, 255, 255, 0.2);
  
  /* Transitions premium */
  --navbar-transition-fast: 0.15s cubic-bezier(0.4, 0, 0.2, 1);
  --navbar-transition-medium: 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  --navbar-transition-slow: 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  --navbar-transition-bounce: 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.app-navbar {
  /* Glassmorphism background moderne */
  background: linear-gradient(135deg,
  rgba(184, 94, 94, 0.15) 30%,
  rgba(143, 175, 19, 0.15) 50%,
  rgba(39, 151, 177, 0.15) 100%);
  
  /* Effet verre premium */
  backdrop-filter: blur(20px) saturate(200%);
  -webkit-backdrop-filter: blur(20px) saturate(200%);

  /* Borders et shadows */
  border-bottom: 1px solid var(--navbar-glass-border);
  box-shadow: var(--navbar-shadow-md);
  
  /* Layout et positionnement */
  padding: 1rem 2rem;
  position: sticky;
  top: 0;
  z-index: 1000;
  
  /* Transitions fluides */
  transition: all var(--navbar-transition-medium);
  
  /* Optimisation performance */
  will-change: box-shadow, backdrop-filter;
  transform: translateZ(0);
}

.app-navbar:hover {
  box-shadow: var(--navbar-shadow-lg);
  backdrop-filter: blur(25px) saturate(200%);
  border-bottom-color: var(--navbar-border-hover);
}

/* Animation d'apparition */
.app-navbar {
  animation: slideDown 0.6s var(--navbar-transition-medium) both;
}

@keyframes slideDown {
  from {
    transform: translateY(-100%);
    opacity: 0;
  }
  to {
    transform: translateY(0);
    opacity: 1;
  }
}

.navbar-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
  position: relative;
}

/* Logo avec gradient ultra-moderne */
.navbar-brand-custom {
  font-size: 2.2rem;
  font-weight: 900;
  text-decoration: none;
  letter-spacing: -0.02em;
  position: relative;
  overflow: hidden;
  
  /* Fallback color pour assurer la visibilité */
  color: var(--navbar-text);
  
  /* Gradient texte animé avec fallback */
  background: linear-gradient(
    135deg,
    var(--navbar-accent) 0%,
    #64d3a8 25%,
    #4ade80 50%,
    var(--navbar-accent) 75%,
    #00f5d4 100%
  );
  background-size: 200% 200%;
  -webkit-background-clip: text;

  background-clip: text;
  
  /* Animation du gradient -webkit-text-fill-color: transparent;*/
  animation: gradientShift 4s ease-in-out infinite;
  
  /* Transitions fluides */
  transition: all var(--navbar-transition-medium);
  
  /* Effet de profondeur */
  filter: drop-shadow(0 2px 4px rgba(0, 212, 170, 0.2));
  
  /* Support des navigateurs qui ne supportent pas background-clip: text */
  @supports not (-webkit-background-clip: text) {
    color: var(--navbar-accent);
    background: none;
  }
}

@keyframes gradientShift {
  0%, 100% {
    background-position: 0% 50%;
  }
  50% {
    background-position: 100% 50%;
  }
}

.navbar-brand-custom:hover {
  transform: translateY(-2px) scale(1.02);
  filter: drop-shadow(0 4px 8px rgba(0, 212, 170, 0.3));
  animation-duration: 4s;
}

/* Effet brillant au survol */
.navbar-brand-custom::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.3),
    transparent
  );
  transition: left 0.6s ease;
}

.navbar-brand-custom:hover::after {
  left: 100%;
}

/* Navigation moderne avec glassmorphism */
.navbar-links {
  background: var(--navbar-glass);
  border-radius: 12px;
  padding: 0.5rem 2rem;
  border: 1px solid var(--navbar-glass-border);
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
  gap: 1rem;
  align-items: center;
}

.nav-item-custom {
  position: relative;
}

/* Liens navigation ultra-modernes */
.nav-link-custom {
  color: var(--navbar-text-secondary);
  text-decoration: none;
  font-size: 1rem;
  font-weight: 500;
  padding: 0.75rem 1.25rem;
  border-radius: 8px;
  position: relative;
  overflow: hidden;
  transition: all var(--navbar-transition-medium);
  display: block;
  
  /* Effet glassmorphism au repos */
  background: transparent;
  border: 1px solid transparent;
}

.nav-link-custom:hover {
  color: var(--navbar-text);
  background: var(--navbar-glass-hover);
  border-color: var(--navbar-glass-border);
  transform: translateY(-2px);
  box-shadow: var(--navbar-shadow-sm);
}

/* Underline animé premium */
.nav-link-custom::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: linear-gradient(
    90deg,
    var(--navbar-accent),
    #4ade80,
    var(--navbar-accent)
  );
  transition: all var(--navbar-transition-bounce);
  transform: translateX(-50%);
  border-radius: 1px;
}

.nav-link-custom:hover::before {
  width: 80%;
}

/* Effet de shimmer au survol */
.nav-link-custom::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.1),
    transparent
  );
  transition: left 0.5s ease;
}

.nav-link-custom:hover::after {
  left: 100%;
}

/* État actif premium */
.nav-link-custom.active-link {
  color: var(--navbar-text);
  background: linear-gradient(
    135deg,
    var(--navbar-accent),
    var(--navbar-accent-hover)
  );
  border-color: var(--navbar-accent);
  box-shadow: var(--navbar-shadow-glow);
  font-weight: 600;
}

.nav-link-custom.active-link::before {
  width: 100%;
  background: rgba(255, 255, 255, 0.3);
  height: 1px;
  bottom: 2px;
}

.nav-link-custom.active-link:hover {
  transform: translateY(-1px) scale(1.02);
  box-shadow: 0 0 25px var(--navbar-accent-glow);
}

.navbar-auth {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 1rem;
}

.welcome-message {
  color: var(--navbar-text-color);
  font-size: 0.9rem;
}

.auth-button {
  background-color: transparent;
  color: var(--navbar-text-color);
  border: 1px solid var(--navbar-text-color);
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.auth-button:hover {
  background-color: var(--navbar-text-color);
  color: var(--navbar-bg-color);
}

.register-link {
  background-color: var(--navbar-hover-color);
  padding: 8px 16px !important;
  border-radius: 4px;
  margin-left: 10px;
}

.register-link:hover {
  background-color: #3aa876;
  color: white !important;
}

/* Media queries pour la responsivité (menu hamburger) */
@media (max-width: 768px) {
  .navbar-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .navbar-brand-custom {
    margin-bottom: 15px;
  }

  .navbar-links {
    width: 100%;
  }

  .nav-list {
    flex-direction: column;
    width: 100%;
  }

  .nav-item-custom {
    margin-left: 0;
    margin-bottom: 10px;
    width: 100%;
  }

  .nav-link-custom {
    padding: 10px 15px;
    text-align: center;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.1);
  }

  .nav-link-custom::after {
    display: none;
  }

  .nav-link-custom.active-link {
    background-color: var(--navbar-active-color);
    color: white;
  }

  .navbar-auth {
    margin-left: 0;
    width: 100%;
    margin-top: 15px;
    flex-direction: column;
    align-items: stretch;
  }

  .welcome-message {
    text-align: center;
    margin-bottom: 10px;
  }

  .auth-button {
    width: 100%;
  }
}

.user-menu {
  position: relative;
}

.user-menu-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  color: var(--navbar-text-color);
}

/* User menu premium */
.user-menu {
  position: relative;
}

.user-menu-button {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  background: var(--navbar-glass);
  border: 1px solid var(--navbar-glass-border);
  border-radius: 12px;
  cursor: pointer;
  padding: 0.5rem 1rem;
  color: var(--navbar-text);
  transition: all var(--navbar-transition-medium);
  backdrop-filter: blur(10px);
}

.user-menu-button:hover {
  background: var(--navbar-glass-hover);
  border-color: var(--navbar-border-hover);
  transform: translateY(-1px);
  box-shadow: var(--navbar-shadow-sm);
}

/* Avatar 3D premium */
.user-avatar-container {
  display: flex;
  align-items: center;
  justify-content: center;
}

.user-avatar-circle {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: linear-gradient(
    135deg,
    var(--navbar-accent),
    var(--navbar-accent-hover),
    #4ade80
  );
  color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 1rem;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  
  /* Amélioration du contraste du texte */
  text-shadow: 
    0 1px 2px rgba(0, 0, 0, 0.5),
    0 0 4px rgba(0, 0, 0, 0.3);
  
  /* Effet 3D premium */
  box-shadow: 
    0 4px 8px rgba(0, 212, 170, 0.2),
    0 0 0 2px rgba(255, 255, 255, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  
  transition: all var(--navbar-transition-medium);
  position: relative;
  overflow: hidden;
}

/* Effet de brillance sur l'avatar */
.user-avatar-circle::before {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(
    45deg,
    transparent,
    rgba(255, 255, 255, 0.1),
    transparent
  );
  transform: rotate(45deg);
  transition: transform 0.6s ease;
}

.user-menu-button:hover .user-avatar-circle {
  transform: scale(1.08) rotate(2deg);
  box-shadow: 
    0 6px 16px rgba(0, 212, 170, 0.3),
    0 0 0 3px rgba(255, 255, 255, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.3);
}

.user-menu-button:hover .user-avatar-circle::before {
  transform: rotate(45deg) translate(100%, 100%);
}

.user-name {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--navbar-text);
  max-width: 140px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  letter-spacing: -0.01em;
}

/* Icône chevron personnalisée */
.chevron-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  transition: transform 0.3s ease;
  opacity: 0.7;
}

.chevron-icon.rotate {
  transform: rotate(180deg);
}

.chevron-icon:hover {
  opacity: 1;
}

/* Menu dropdown glassmorphism premium */
.user-dropdown {
  position: absolute;
  top: calc(100% + 0.5rem);
  right: 0;
  min-width: 240px;
  z-index: 1001;
  
  /* Glassmorphism background */
  background: linear-gradient(
    135deg,
    rgba(184, 94, 94, 0.15) 30%,
    rgba(143, 175, 19, 0.15) 50%,
    rgba(39, 151, 177, 0.15) 100%);

  backdrop-filter: blur(20px) saturate(200%);
  -webkit-backdrop-filter: blur(20px) saturate(200%);
  
  /* Borders et shadows premium */
  border: 1px solid var(--navbar-glass-border);
  border-radius: 16px;
  box-shadow: 
    var(--navbar-shadow-lg),
    0 0 40px rgba(0, 212, 170, 0.1);
  
  /* Animation d'apparition */
  animation: dropdownSlideIn 0.3s var(--navbar-transition-bounce) both;
  
  /* Padding interne */
  padding: 0.5rem;
  overflow: hidden;
}

@keyframes dropdownSlideIn {
  from {
    opacity: 0;
    transform: translateY(-10px) scale(0.95);
  }
  to {
    opacity: 1;
    transform: translateY(0) scale(1);
  }
}

/* Items du dropdown modernes */
.dropdown-item {
  display: flex;
  align-items: center;
  gap: 0.875rem;
  padding: 0.875rem 1.125rem;
  color: var(--navbar-text-secondary);
  text-decoration: none;
  border-radius: 10px;
  border: none;
  width: 100%;
  text-align: left;
  background: transparent;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 500;
  transition: all var(--navbar-transition-medium);
  position: relative;
  overflow: hidden;
}

.dropdown-item:hover {
  color: var(--navbar-text);
  background: var(--navbar-glass-hover);
  transform: translateX(4px);
  box-shadow: var(--navbar-shadow-sm);
}

/* Effet shimmer sur les items */
.dropdown-item::after {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(
    90deg,
    transparent,
    rgba(255, 255, 255, 0.05),
    transparent
  );
  transition: left 0.4s ease;
}

.dropdown-item:hover::after {
  left: 100%;
}

/* Icons emoji modernes */
.dropdown-icon {
  font-size: 1.2rem;
  width: 24px;
  height: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 6px;
  background: var(--navbar-glass);
  border: 1px solid var(--navbar-glass-border);
  transition: all var(--navbar-transition-medium);
}

.dropdown-item:hover .dropdown-icon {
  background: var(--navbar-accent);
  border-color: var(--navbar-accent);
  transform: scale(1.1) rotate(5deg);
}

/* Séparateur élégant */
.dropdown-divider {
  height: 1px;
  background: linear-gradient(
    90deg,
    transparent,
    var(--navbar-glass-border),
    transparent
  );
  margin: 0.5rem 0;
  border-radius: 0.5px;
}

/* Bouton de déconnexion spécial */
.dropdown-item.logout {
  color: #ff6b6b;
  margin-top: 0.25rem;
}

.dropdown-item.logout:hover {
  background: rgba(255, 107, 107, 0.1);
  color: #ff5252;
}

.dropdown-item.logout .dropdown-icon {
  background: rgba(255, 107, 107, 0.1);
  border-color: rgba(255, 107, 107, 0.2);
}

.dropdown-item.logout:hover .dropdown-icon {
  background: #ff6b6b;
  border-color: #ff6b6b;
  color: white;
}

@media (max-width: 768px) {
  .user-dropdown {
    position: static;
    box-shadow: none;
    margin-top: 1rem;
  }

  .user-menu-button {
    width: 100%;
    justify-content: center;
  }
}
</style>