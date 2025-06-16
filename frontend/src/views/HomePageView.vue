<template>
  <div class="home-page-container">
    <header class="app-header">
      <h4>{{ welcomeMessage }}</h4>
      <p v-if="user" class="user-info">{{ getUserStatusMessage }}</p>
    </header>

    <div class="main-content">
      <CategoryList
          :selectedCategoryId="selectedCategoryId"
          @category-selected="handleCategorySelected"/>

      <ProductList
          :is-title-center="true"
          :categoryId="selectedCategoryId"
          :categoryName="selectedCategoryName"/>
    </div>
  </div>
</template>

<script>
import CategoryList from '@/components/categoryList.vue';
import ProductList from '@/components/productList.vue';
import categoryService from '@/services/categoryService.js';
import authService from '@/services/authService.js';

export default {
  name: 'HomePage',
  components: {
    CategoryList,
    ProductList
  },
  data() {
    return {
      selectedCategoryId: null,
      categories: [],
      selectedCategoryName: 'Tous les produits',
      user: null,
      isAuthenticated: false
    };
  },
  computed: {
    welcomeMessage() {
      if (this.isAuthenticated && this.user) {
        const nom = this.user.nom || '';
        const prenom = this.user.prenom || '';
        
        if (prenom && nom) {
          return `Bienvenue ${prenom} ${nom} !`;
        } else if (prenom) {
          return `Bienvenue ${prenom} !`;
        } else if (nom) {
          return `Bienvenue ${nom} !`;
        } else if (this.user.email) {
          const emailName = this.user.email.split('@')[0];
          return `Bienvenue ${emailName} !`;
        }
      }
      // Message par défaut si non connecté ou pas d'informations
      return 'Bienvenue sur notre boutique !';
    },
    
    getUserStatusMessage() {
      if (!this.user) return '';
      
      const messages = [
        'Découvrez nos dernières nouveautés',
        'Explorez nos catégories et trouvez ce que vous cherchez',
        'Profitez de vos achats en ligne !'
      ];
      
      // Choisir un message basé sur l'ID utilisateur pour cohérence
      const messageIndex = (this.user.id || 0) % messages.length;
      return messages[messageIndex];
    }
  },
  async created() {
    // Vérifier l'authentification
    this.checkAuthentication();
    
    // Charger toutes les catégories au démarrage pour pouvoir trouver leur nom
    try {
      this.categories = await categoryService.getAllCategories();
    } catch (error) {
      console.error("Erreur lors du chargement des catégories pour le nom :", error);
    }
  },
  mounted() {
    // Écouter les événements d'authentification
    window.addEventListener('user-authenticated', this.onUserAuthenticated);
    window.addEventListener('user-logged-out', this.onUserLoggedOut);
  },
  beforeUnmount() {
    // Nettoyer les événements
    window.removeEventListener('user-authenticated', this.onUserAuthenticated);
    window.removeEventListener('user-logged-out', this.onUserLoggedOut);
  },
  methods: {
    handleCategorySelected(categoryId) {
      this.selectedCategoryId = categoryId;
      // Mettre à jour le nom de la catégorie pour le titre de ProductList
      if (categoryId === null) {
        this.selectedCategoryName = 'Tous les produits';
      } else {
        const category = this.categories.find(cat => cat.id === categoryId);
        this.selectedCategoryName = category ? category.nom : 'Catégorie inconnue';
      }
    },
    
    checkAuthentication() {
      // Vérifier si l'utilisateur est connecté
      this.isAuthenticated = authService.isAuthenticated();
      if (this.isAuthenticated) {
        this.user = authService.getCurrentUser();
        console.log('Utilisateur connecté:', this.user);
      } else {
        this.user = null;
        console.log('Aucun utilisateur connecté');
      }
    },
    
    onUserAuthenticated() {
      // Méthode appelée quand un utilisateur se connecte
      console.log('Événement: Utilisateur authentifié');
      this.checkAuthentication();
    },
    
    onUserLoggedOut() {
      // Méthode appelée quand un utilisateur se déconnecte
      console.log('Événement: Utilisateur déconnecté');
      this.isAuthenticated = false;
      this.user = null;
    }
  }
};
</script>

<style scoped>
.home-page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.app-header {
  //background-color: #42b983; /* Couleur d'en-tête */
  //color: white;
  color: #b86161;
  padding: 20px 0;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background: linear-gradient(135deg, #f8f9fa, #e9ecef);
  border-bottom: 1px solid #dee2e6;
}

.app-header h4 {
  margin: 0 0 8px 0;
  font-size: 1.8em;
  font-weight: 600;
  color: #2c3e50;
  transition: color 0.3s ease;
}

.user-info {
  margin: 0;
  font-size: 1.1em;
  color: #6c757d;
  font-style: italic;
  animation: fadeIn 0.5s ease-in;
}

/* Animation pour l'apparition du message utilisateur */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Animation pour le message de bienvenue personnalisé */
.app-header h4 {
  animation: slideInFromTop 0.6s ease-out;
}

@keyframes slideInFromTop {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* Effet hover sur le header */
.app-header:hover h4 {
  color: #42b983;
}

.app-header h1 {
  margin: 0;
  font-size: 2em;
}

.main-content {
  display: flex; /* Utilise Flexbox pour aligner le menu et la liste de produits */
  flex-grow: 1; /* Permet au contenu principal de prendre l'espace restant */
}
</style>