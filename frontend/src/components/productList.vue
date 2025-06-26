<template>
  <div class="product-list-container">
    <div class="container mt-4">
      <!-- En-tête -->
      <div class="row mb-4">
        <div class="col">
          <h2 class="text-center mb-3">
            <i class="fas fa-tshirt me-2"></i>
            {{ categoryName }}
          </h2>
          <p class="text-center text-muted">
            {{ categoryId ? `Découvrez notre sélection de ${categoryName.toLowerCase()}` : 'Découvrez notre sélection de produits de qualité' }}
          </p>
        </div>
      </div>

      <!-- Filtres et recherche -->
      <div class="row mb-4">
        <div class="col-md-6">
          <div class="input-group">
            <span class="input-group-text">
              <i class="fas fa-search"></i>
            </span>
            <input
              type="text"
              class="form-control"
              placeholder="Rechercher un produit..."
              v-model="searchTerm"
              @input="filterProducts"
            >
          </div>
        </div>
        <div class="col-md-6">
          <select class="form-select" v-model="selectedCategory" @change="filterProducts">
            <option value="">Toutes les catégories</option>
            <option v-for="category in categories" :key="category.id" :value="category.id">
              {{ category.nom }}
            </option>
          </select>
        </div>
      </div>

      <!-- État de chargement -->
      <div v-if="loading" class="text-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Chargement...</span>
        </div>
        <p class="mt-3">Chargement des produits...</p>
      </div>

      <!-- Message si aucun produit -->
      <div v-else-if="filteredProducts.length === 0" class="text-center py-5">
        <i class="fas fa-box-open fa-3x text-muted mb-3"></i>
        <h4 class="text-muted">Aucun produit trouvé</h4>
        <p class="text-muted">
          {{ searchTerm || selectedCategory ? 'Aucun produit ne correspond à vos critères.' : 'Aucun produit disponible pour le moment.' }}
        </p>
        <button v-if="searchTerm || selectedCategory" @click="clearFilters" class="btn btn-outline-primary">
          <i class="fas fa-times me-2"></i>
          Effacer les filtres
        </button>
      </div>

      <!-- Liste des produits -->
      <div v-else class="row">
        <div
          v-for="product in paginatedProducts"
          :key="product.id"
          class="col-lg-3 col-md-4 col-sm-6 mb-4"
        >
          <div class="card product-card h-100">
            <!-- Image du produit -->
            <div class="product-image-container">
              <img
                :src="product.thumbnailUrl"
                :alt="product.nom"
                class="card-img-top product-image"
                @error="handleImageError"
              >
              <div class="product-overlay">
                <router-link :to="`/product/${product.id}`" class="btn btn-primary btn-sm">
                  <i class="fas fa-eye me-1"></i>
                  Voir détails
                </router-link>
              </div>
            </div>

            <!-- Contenu de la carte -->
            <div class="card-body d-flex flex-column">
              <h5 class="card-title product-title">{{ product.nom }}</h5>
              <p class="card-text product-description">{{ product.description }}</p>
              
              <div class="product-meta mb-3">
                <span class="badge bg-secondary me-2">{{ product.marque }}</span>
                <span class="badge bg-info me-2">{{ product.genre }}</span>
                <span class="badge bg-warning">{{ product.taille }}</span>
              </div>

              <div class="mt-auto">
                <div class="d-flex justify-content-between align-items-center">
                  <span class="product-price">{{ formatPrice(product.prix) }}</span>
                  <button
                    @click="addToCart(product)"
                    class="btn btn-success btn-sm"
                    :disabled="addingToCart === product.id"
                  >
                    <span v-if="addingToCart === product.id" class="spinner-border spinner-border-sm me-1"></span>
                    <i v-else class="fas fa-cart-plus me-1"></i>
                    {{ addingToCart === product.id ? 'Ajout...' : 'Ajouter' }}
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Pagination -->
      <div v-if="filteredProducts.length > 0" class="row mt-4">
        <div class="col">
          <div class="d-flex justify-content-between align-items-center">
            <div class="text-muted">
              Affichage {{ (currentPage - 1) * itemsPerPage + 1 }} - {{ Math.min(currentPage * itemsPerPage, filteredProducts.length) }} 
              sur {{ filteredProducts.length }} produits
            </div>
            <nav aria-label="Navigation des produits">
              <ul class="pagination mb-0">
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                  <a class="page-link" href="#" @click.prevent="changePage(1)" title="Première page">
                    <i class="fas fa-angle-double-left"></i>
                  </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === 1 }">
                  <a class="page-link" href="#" @click.prevent="changePage(currentPage - 1)" title="Page précédente">
                    <i class="fas fa-chevron-left"></i>
                  </a>
                </li>
                
                <li v-if="visiblePages[0] > 1" class="page-item disabled">
                  <span class="page-link">...</span>
                </li>
                
                <li
                  v-for="page in visiblePages"
                  :key="page"
                  class="page-item"
                  :class="{ active: page === currentPage }"
                >
                  <a class="page-link" href="#" @click.prevent="changePage(page)">{{ page }}</a>
                </li>
                
                <li v-if="visiblePages[visiblePages.length - 1] < totalPages" class="page-item disabled">
                  <span class="page-link">...</span>
                </li>
                
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                  <a class="page-link" href="#" @click.prevent="changePage(currentPage + 1)" title="Page suivante">
                    <i class="fas fa-chevron-right"></i>
                  </a>
                </li>
                <li class="page-item" :class="{ disabled: currentPage === totalPages }">
                  <a class="page-link" href="#" @click.prevent="changePage(totalPages)" title="Dernière page">
                    <i class="fas fa-angle-double-right"></i>
                  </a>
                </li>
              </ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import productService from '@/services/productService.js';
import categoryService from '@/services/categoryService.js';

export default {
  name: 'ProductList',
  props: {
    categoryId: {
      type: Number,
      default: null
    },
    categoryName: {
      type: String,
      default: 'Tous les produits'
    }
  },
  data() {
    return {
      products: [],
      categories: [],
      filteredProducts: [],
      loading: true,
      searchTerm: '',
      selectedCategory: '',
      addingToCart: null,
      currentPage: 1,
      itemsPerPage: 12
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.filteredProducts.length / this.itemsPerPage);
    },
    paginatedProducts() {
      const start = (this.currentPage - 1) * this.itemsPerPage;
      const end = start + this.itemsPerPage;
      return this.filteredProducts.slice(start, end);
    },
    visiblePages() {
      const total = this.totalPages;
      const current = this.currentPage;
      const delta = 2;
      
      let start = Math.max(1, current - delta);
      let end = Math.min(total, current + delta);
      
      if (end - start < 4) {
        if (start === 1) {
          end = Math.min(total, start + 4);
        } else {
          start = Math.max(1, end - 4);
        }
      }
      
      const pages = [];
      for (let i = start; i <= end; i++) {
        pages.push(i);
      }
      return pages;
    }
  },
  watch: {
    categoryId(newVal) {
      if (newVal !== null) {
        this.selectedCategory = newVal.toString();
        this.filterProducts();
      } else {
        this.selectedCategory = '';
        this.filterProducts();
      }
    },
    categoryName(newVal) {
      // Le titre se mettra à jour automatiquement grâce à la réactivité de Vue
    }
  },
  async mounted() {
    await this.loadData();
  },
  methods: {
    async loadData() {
      try {
        this.loading = true;
        
        const [products, categories] = await Promise.all([
          productService.getAllProducts(),
          categoryService.getAllCategories()
        ]);
        
        console.log('=== CHARGEMENT DES DONNÉES ===');
        console.log('Produits chargés:', products.length);
        console.log('Catégories chargées:', categories.length);
        console.log('Props categoryId:', this.categoryId);
        console.log('Props categoryName:', this.categoryName);
        
        this.products = products;
        this.categories = categories;
        
        if (this.categoryId !== null) {
          this.selectedCategory = this.categoryId.toString();
          console.log('CategoryId appliqué:', this.selectedCategory);
        }
        
        this.filteredProducts = [...products];
        this.filterProducts();
        
      } catch (error) {
        console.error('Erreur lors du chargement des données:', error);
      } finally {
        this.loading = false;
      }
    },

    filterProducts() {
      let filtered = [...this.products];
      
      console.log('=== DÉBUT FILTRAGE ===');
      console.log('Produits totaux:', this.products.length);
      console.log('Catégorie sélectionnée:', this.selectedCategory);
      console.log('Type de selectedCategory:', typeof this.selectedCategory);

      if (this.searchTerm) {
        const term = this.searchTerm.toLowerCase();
        filtered = filtered.filter(product =>
          product.nom.toLowerCase().includes(term) ||
          product.description.toLowerCase().includes(term) ||
          product.marque.toLowerCase().includes(term)
        );
        console.log('Après filtrage par recherche:', filtered.length);
      }

      if (this.selectedCategory) {
        console.log('Filtrage par catégorie ID:', this.selectedCategory);
        console.log('Exemple de produit:', filtered[0]);
        
        filtered = filtered.filter(product => {
          const productCategoryId = product.categoryId;
          
          console.log(`Produit ${product.nom}:`, {
            'product.categoryId': productCategoryId,
            'selectedCategory (string)': this.selectedCategory,
            'selectedCategory (number)': parseInt(this.selectedCategory),
            'comparaison': productCategoryId === parseInt(this.selectedCategory)
          });
          
          return productCategoryId === parseInt(this.selectedCategory);
        });
        
        console.log('Après filtrage par catégorie:', filtered.length);
      }

      this.filteredProducts = filtered;
      this.currentPage = 1;
      console.log('=== FIN FILTRAGE ===');
    },

    clearFilters() {
      this.searchTerm = '';
      this.selectedCategory = '';
      this.filteredProducts = [...this.products];
      this.currentPage = 1;
    },

    changePage(page) {
      if (page >= 1 && page <= this.totalPages) {
        this.currentPage = page;
      }
    },

    formatPrice(price) {
      return new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR'
      }).format(price);
    },

    handleImageError(event) {
      event.target.src = '/placeholder-image.jpg';
    },

    async addToCart(product) {
      try {
        this.addingToCart = product.id;
        
        await new Promise(resolve => setTimeout(resolve, 500));
        
        this.$emit('add-to-cart', product);
        
        this.$toast?.success(`${product.nom} ajouté au panier !`);
        
      } catch (error) {
        console.error('Erreur lors de l\'ajout au panier:', error);
        this.$toast?.error('Erreur lors de l\'ajout au panier');
      } finally {
        this.addingToCart = null;
      }
    }
  }
};
</script>

<style scoped>
.product-list-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}

.product-card {
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  border: none;
  border-radius: 15px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.product-image-container {
  position: relative;
  overflow: hidden;
  height: 200px;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.product-card:hover .product-image {
  transform: scale(1.05);
}

.product-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.7);
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
  transition: opacity 0.3s ease;
}

.product-card:hover .product-overlay {
  opacity: 1;
}

.product-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 0.5rem;
  line-height: 1.3;
}

.product-description {
  font-size: 0.9rem;
  color: #6c757d;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.product-meta {
  margin-top: auto;
}

.product-price {
  font-size: 1.2rem;
  font-weight: 700;
  color: #28a745;
}

.badge {
  font-size: 0.75rem;
}

@media (max-width: 768px) {
  .product-image-container {
    height: 150px;
  }
  
  .product-title {
    font-size: 1rem;
  }
  
  .product-description {
    font-size: 0.8rem;
  }
}

.product-card {
  animation: fadeInUp 0.6s ease forwards;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.product-card:nth-child(1) { animation-delay: 0.1s; }
.product-card:nth-child(2) { animation-delay: 0.2s; }
.product-card:nth-child(3) { animation-delay: 0.3s; }
.product-card:nth-child(4) { animation-delay: 0.4s; }
</style> 