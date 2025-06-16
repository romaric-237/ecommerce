<template>
  <nav class="category-menu">
    <h5>Catégories</h5>
    <ul v-if="categories.length > 0">
      <li>
        <button
            @click="selectCategory(null)"
            :class="{ 'active': selectedCategoryId === null }">
          Tous les produits
        </button>
      </li>
      <li v-for="category in categories" :key="category.id">
        <button
            @click="selectCategory(category.id)"
            :class="{ 'active': selectedCategoryId === category.id }"
        >
          {{ category.nom }}
        </button>
      </li>
    </ul>
    <div v-if="error" class="error-message">
      <i class="error-icon">⚠️</i>
      {{ error }}
      <button @click="retryFetch" class="retry-button">Réessayer</button>
    </div>
    
    <div v-else-if="isLoading" class="loading-categories">
      <div class="spinner"></div>
      Chargement des catégories...
    </div>
    
    <div v-else-if="categories.length === 0" class="no-categories">
      <i class="info-icon">ℹ️</i>
      Aucune catégorie disponible.
    </div>
  </nav>
</template>

<script>
import categoryService from '@/services/categoryService';

export default {
  name: 'CategoryMenu',
  props: {
    selectedCategoryId: {
      type: Number,
      default: null
    },
    minimumLoadingTime: {
      type: Number,
      default: 500,
      validator: (value) => value >= 0 && value <= 5000 // Entre 0 et 5 secondes
    },
    showLoadingSpinner: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      categories: [],
      isLoading: true,
      error: null
    };
  },
  async created() {
    await this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      this.isLoading = true;
      this.error = null;
      
      // Enregistrer le temps de début pour assurer un délai minimum
      const startTime = Date.now();
      const minLoadTime = this.minimumLoadingTime; // Utiliser la prop
      
      try {
        const categories = await categoryService.getAllCategories();
        
        // S'assurer que categories est toujours un tableau
        this.categories = Array.isArray(categories) ? categories : [];
        
        console.log(`Chargé ${this.categories.length} catégories`);
        
        // Calculer le temps écoulé et ajouter un délai si nécessaire
        const elapsedTime = Date.now() - startTime;
        const remainingTime = Math.max(0, minLoadTime - elapsedTime);
        
        if (remainingTime > 0) {
          await new Promise(resolve => setTimeout(resolve, remainingTime));
        }
        
      } catch (err) {
        this.error = "Impossible de charger les catégories. Veuillez réessayer plus tard.";
        console.error('Erreur lors du chargement des catégories:', err);
        this.categories = [];
        
        // Même délai minimum en cas d'erreur pour cohérence UX
        const elapsedTime = Date.now() - startTime;
        const remainingTime = Math.max(0, minLoadTime - elapsedTime);
        
        if (remainingTime > 0) {
          await new Promise(resolve => setTimeout(resolve, remainingTime));
        }
      } finally {
        this.isLoading = false;
      }
    },
    
    retryFetch() {
      this.fetchCategories();
    },
    
    selectCategory(categoryId) {
      // Émet un événement pour informer le parent de la catégorie sélectionnée
      this.$emit('category-selected', categoryId);
    }
  }
};
</script>

<style scoped>
.category-menu {
  width: 200px; /* Largeur du menu latéral */
  padding: 20px;
  background-color: #f8f8f8;
  border-right: 1px solid #eee;
  box-shadow: 2px 0 5px rgba(0,0,0,0.05);
  min-height: calc(100vh - 60px); /* Ajustez si vous avez un header/footer */
  box-sizing: border-box; /* Inclut padding et border dans la largeur */
}

.category-menu h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
}

.category-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-menu li {
  margin-bottom: 10px;
}

.category-menu button {
  display: block;
  width: 100%;
  padding: 10px 15px;
  text-align: left;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  color: #555;
  transition: background-color 0.2s ease, color 0.2s ease, border-color 0.2s ease;
}

.category-menu button:hover {
  background-color: #eef;
  color: #007bff;
  border-color: #aaddff;
}

.category-menu button.active {
  background-color: #007bff;
  color: #fff;
  border-color: #007bff;
  font-weight: bold;
}

.loading-categories, .no-categories {
  text-align: center;
  color: #777;
  font-style: italic;
  margin-top: 20px;
  padding: 15px;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.error-message {
  text-align: center;
  color: #dc3545;
  background-color: #f8d7da;
  border: 1px solid #f5c6cb;
  border-radius: 8px;
  padding: 15px;
  margin: 15px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
  font-size: 0.9rem;
}

.error-icon, .info-icon {
  font-size: 1.2rem;
}

.retry-button {
  background-color: #dc3545;
  color: white;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.8rem;
  transition: background-color 0.3s ease;
}

.retry-button:hover {
  background-color: #c82333;
}

/* Spinner pour le chargement - Version améliorée */
.spinner {
  width: 36px;
  height: 36px;
  border: 4px solid #e3e3e3;
  border-top: 4px solid #42b983;
  border-right: 4px solid #42b983;
  border-radius: 50%;
  animation: spin 1.2s linear infinite;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
    border-top-color: #42b983;
    border-right-color: transparent;
  }
  25% {
    border-top-color: transparent;
    border-right-color: #42b983;
  }
  50% {
    transform: rotate(180deg);
    border-top-color: transparent;
    border-right-color: transparent;
    border-bottom-color: #42b983;
  }
  75% {
    border-bottom-color: transparent;
    border-left-color: #42b983;
  }
  100% {
    transform: rotate(360deg);
    border-top-color: #42b983;
    border-right-color: transparent;
  }
}


.category-menu ul {
  animation: slideIn 0.4s ease-out;
}

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

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateX(-20px);
  }
  to {
    opacity: 1;
    transform: translateX(0);
  }
}


.no-categories {
  background: linear-gradient(135deg, #fff3cd, #ffeaa7);
  border-radius: 8px;
  border: 1px solid #ffeaa7;
  color: #856404;
  font-weight: 500;
}
</style>