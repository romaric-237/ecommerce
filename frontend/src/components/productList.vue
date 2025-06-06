<template>
  <div class="product-list-container">
    <h2 :class="{ 'text-center': isTitleCenter }">{{ title }}</h2>
    <div v-if="products.length === 0 && !isLoading" class="no-products-message">
      Aucun produit n'est associé à cette catégorie pour le moment.
    </div>

    <div v-if="isLoading" class="loading-message">
      Chargement des produits...
    </div>

    <div v-else class="product-grid">
      <router-link v-for="product in products" :key="product.id"
                   :to="{ name: 'product-detail', params: { id: product.id } }"
                   class="product-item">
        <div class="thumbnail-wrapper" @mouseover="showTooltip(product)" @mouseleave="hideTooltip">
          <img :src="getProductImageUrl(product)" :alt="product.nom" class="product-thumbnail"/>
          <div v-if="hoveredProduct === product" class="tooltip">
            {{ product.nom }}
          </div>
        </div>
        <p class="product-item-name">{{ product.nom }}</p>
        <p class="product-item-price">{{ product.formattedPrice }}</p>
      </router-link>
    </div>
  </div>
</template>

<script>
import productService from '@/services/productService';
import unsplashService from '@/services/unsplashService';

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
    },
    isTitleCenter: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      products: [],
      isLoading: true,
      error: null,
      hoveredProduct: null,
      unsplashImageCache: new Map()
    };
  },
  watch: {
    categoryId: {
      immediate: true, // Exécute le handler au montage initial
      handler(newId) {
        this.fetchProducts(newId);
      }
    }
  },
  computed: {
    title() {
      return this.isTitleCenter ? `Produits : ${this.categoryName}` : 'Nos produits';
    }
  },
  methods: {
    async fetchProducts(categoryId) {
      this.isLoading = true;
      this.error = null;
      try {
        if (categoryId) {
          this.products = await productService.getProductsByCategory(categoryId);
        } else {
          this.products = await productService.getAllProducts();
        }
        for (const product of this.products) {
          const query = product.nom || product.categoryNom || product.marque || 'product';
          // Vérifier le cache avant de faire une requête
          if (!this.unsplashImageCache.has(query)) {
            const imageUrl = await unsplashService.searchImage(query);
            this.unsplashImageCache.set(query, imageUrl);
          }
        }
      } catch (err) {
        this.error = "Impossible de charger les produits. Veuillez réessayer plus tard.";
        console.error(err);
        this.products = [];
      } finally {
        this.isLoading = false;
      }
    },
    showTooltip(product) {
      this.hoveredProduct = product;
    },
    hideTooltip() {
      this.hoveredProduct = null;
    },
    getProductImageUrl(product) {
      const query = product.nom || product.categoryNom || product.marque || 'product';
      return this.unsplashImageCache.get(query) || 'https://via.placeholder.com/150?text=Produit';

    }
  }
};
</script>

<style scoped>
.product-list-container {
  padding: 20px;
  //max-width: 1200px;
  //margin: 0 auto;
  flex-grow: 1;
}

h2 {
  //text-align: center;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.no-products-message, .loading-message {
  text-align: center;
  color: #777;
  font-style: italic;
  margin-top: 50px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); /* 150px est la taille des vignettes */
  gap: 20px;
  justify-content: center;
}

.product-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 10px;
  background-color: #fff;
  transition: transform 0.2s ease-in-out;
  position: relative; /* Pour positionner l'info-bulle */
}

.product-item:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.thumbnail-wrapper {
  position: relative; /* Pour positionner l'info-bulle */
  width: 100%;
  height: 150px; /* Taille fixe pour les vignettes */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden; /* Pour que l'image ne dépasse pas */
}

.product-thumbnail {
  max-width: 100%;
  max-height: 100%;
  display: block;
  object-fit: contain; /* L'image s'adapte sans être coupée */
}

/* Style de l'info-bulle */
.tooltip {
  position: absolute;
  top: 100%; /* Juste en dessous de l'image */
  left: 50%;
  transform: translateX(-50%);
  background-color: rgba(0, 0, 0, 0.8);
  color: #fff;
  padding: 5px 10px;
  border-radius: 4px;
  white-space: nowrap;
  z-index: 10;
  font-size: 0.9em;
  pointer-events: none; /* Empêche l'info-bulle de bloquer les événements de souris */
  opacity: 0; /* Caché par défaut */
  transition: opacity 0.3s ease;
}

.thumbnail-wrapper:hover .tooltip {
  opacity: 1; /* Apparaît au survol */
}

.text-center {
  text-align: left;
}

/* Vous pouvez ajouter d'autres styles pour les détails du produit si vous en affichez */
/* .product-name {
  margin-top: 10px;
  font-weight: bold;
  text-align: center;
} */
</style>