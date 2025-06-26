<template>
  <div class="categories-section">
    <h2 class="text-center mb-4">Explorez Nos Collections</h2>
    <p class="text-center text-muted mb-5">Découvrez notre sélection de produits par catégorie</p>

    <div v-if="isLoading" class="d-flex flex-column align-items-center justify-content-center py-5">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
      <p class="mt-3 text-muted">Chargement des catégories...</p>
    </div>

    <div v-else-if="error" class="alert alert-danger text-center" role="alert">
      <i class="fas fa-exclamation-circle me-2"></i>
      {{ error }}
    </div>

    <div v-else-if="categories.length === 0" class="text-center py-5">
      <i class="fas fa-folder-open text-muted mb-3" style="font-size: 3rem;"></i>
      <h3 class="h4 text-muted">Aucune catégorie disponible</h3>
      <p class="text-muted">Les catégories seront bientôt disponibles.</p>
    </div>

    <div v-else class="row g-4">
      <div v-for="category in categories"
           :key="category.id"
           class="col-12 col-md-6 col-lg-4">
        <div class="card card-hover h-100" @click="selectCategory(category.id)">
          <div class="card-body text-center">
            <i class="fas fa-folder mb-3" style="font-size: 1.8rem; color: var(--bs-primary);"></i>
            <h3 class="h5 fw-bold mb-2">{{ category.nom }}</h3>
            <p class="text-muted small mb-0">{{ category.description }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import categoryService from '@/services/categoryService';

export default {
  name: 'CategoryList',
  props: {
    selectedCategoryId: {
      type: Number,
      default: null
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
      try {
        this.categories = await categoryService.getAllCategories();
        console.log('Catégories chargées:', this.categories);
      } catch (err) {
        console.error('Erreur détaillée:', err);
        this.error = "Impossible de charger les catégories. Veuillez réessayer plus tard.";
      } finally {
        this.isLoading = false;
      }
    },
    selectCategory(categoryId) {
      this.$emit('category-selected', categoryId);
    }
  }
};
</script>

<style scoped>
.categories-section {
  padding: 2rem 0;
}

.card-hover {
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0,0,0,0.15);
  border-color: var(--bs-primary);
}

.card-hover::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(66, 185, 131, 0.1), transparent);
  transition: left 0.5s ease;
}

.card-hover:hover::before {
  left: 100%;
}

.card-body {
  padding: 2rem;
  position: relative;
  z-index: 1;
}

.card-hover i {
  transition: transform 0.3s ease, color 0.3s ease;
}

.card-hover:hover i {
  transform: scale(1.1);
  color: var(--bs-primary) !important;
}

@media (max-width: 768px) {
  .categories-section {
    padding: 1rem 0;
  }
  
  .card-body {
    padding: 1.5rem;
  }
}
</style>