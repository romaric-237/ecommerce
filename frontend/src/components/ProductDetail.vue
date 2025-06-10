<template>
  <div class="product-detail-container">
    <div v-if="isLoading" class="loading-message">
      Chargement du produit...
    </div>

    <div v-else-if="error" class="error-message">
      {{ error }}
    </div>

    <div v-else-if="product" class="product-card">
      <div class="product-image-section">
        <img :src="displayedImageUrl"
             :alt="product.nom" class="product-image" />
      </div>
      <div class="product-info-section">
        <h2 class="product-name">{{ product.nom }}</h2>
        <p class="product-brand">Marque: <strong>{{ product.marque || 'Non spécifiée' }}</strong></p>
        <p class="product-price">Prix: <strong>{{ product.prix }} €</strong></p>

        <div class="product-description-section">
          <h3>Description</h3>
          <p>{{ product.description || 'Pas de description disponible.' }}</p>
        </div>

        <div class="product-specs">
          <p>Genre: <span>{{ product.genre || 'Non spécifié' }}</span></p>
          <p>Taille: <span>{{ product.taille || 'Non spécifiée' }}</span></p>
          <p v-if="product.categoryNom">Catégorie: <span>{{ product.categoryNom }}</span></p>
        </div>

        <div class="action-buttons">
          <button @click="addToCart(product)" class="btn-add-to-cart">Ajouter au panier</button>
          <button @click="goBack" class="btn-go-back">Retour</button>
        </div>
      </div>
    </div>

    <div v-else class="not-found-message">
      Produit non trouvé.
    </div>
  </div>
</template>

<script>
import productService from '@/services/productService';
import unsplashService from "@/services/unsplashService.js";

export default {
  name: 'ProductDetail',
  props: {
    id: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      product: null,
      isLoading: true,
      error: null,
      unsplashImageUrl: null
    };
  },
  computed: {
    displayedImageUrl() {
      return this.unsplashImageUrl || 'https://via.placeholder.com/400x300?text=Image+Produit';
    }
  },
  async created() {
    await this.fetchProduct();
  },
  watch: {
    id: 'fetchProduct'
  },
  methods: {
    async fetchProduct() {
      this.isLoading = true;
      this.error = null;
      this.product = null;
      this.unsplashImageUrl = null;

      try {
        const fetchedProduct = await productService.getProductById(this.id);
        console.log('Produit récupéré:', fetchedProduct); // Pour le débogage
        this.product = fetchedProduct;
        if (this.product) {
          const query = this.product.nom || this.product.categoryNom || this.product.marque || 'product';
          this.unsplashImageUrl = await unsplashService.searchImage(query);
        }
      } catch (err) {
        if (err.response && err.response.status === 404) {
          this.error = "Produit non trouvé.";
        } else {
          this.error = "Erreur lors du chargement du produit. Veuillez réessayer.";
        }
        console.error("Erreur de chargement du produit:", err);
      } finally {
        this.isLoading = false;
      }
    },
    addToCart(product) {
      console.log(`Produit ${product.nom} ajouté au panier !`);
      alert(`"${product.nom}" a été ajouté au panier !`);
    },
    goBack() {
      this.$router.go(-1);
    }
  }
};
</script>

<style scoped>
.product-detail-container {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.product-card {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  overflow: hidden;
}

.product-image-section {
  padding: 1rem;
}

.product-image {
  width: 100%;
  height: auto;
  border-radius: 4px;
  object-fit: cover;
}

.product-info-section {
  padding: 2rem;
}

.product-name {
  font-size: 2rem;
  margin-bottom: 1rem;
  color: #333;
}

.product-brand, .product-price {
  font-size: 1.2rem;
  margin-bottom: 0.5rem;
  color: #666;
}

.product-price {
  font-size: 1.5rem;
  color: #2c3e50;
  font-weight: bold;
}

.product-description-section {
  margin: 2rem 0;
}

.product-description-section h3 {
  color: #333;
  margin-bottom: 1rem;
}

.product-description-section p {
  line-height: 1.6;
  color: #666;
  font-size: 1.05em;
}

.product-specs {
  margin: 2rem 0;
}

.product-specs p {
  margin: 0.5rem 0;
  color: #666;
}

.product-specs span {
  font-weight: bold;
  color: #333;
}

.action-buttons {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-add-to-cart, .btn-go-back {
  padding: 0.8rem 1.5rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.3s ease;
}

.btn-add-to-cart {
  background-color: #4f46e5;
  color: white;
}

.btn-add-to-cart:hover {
  background-color: #4338ca;
}

.btn-go-back {
  background-color: #e5e7eb;
  color: #374151;
}

.btn-go-back:hover {
  background-color: #d1d5db;
}

.loading-message, .error-message, .not-found-message {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
  color: #666;
}

.error-message {
  color: #dc2626;
}

@media (max-width: 768px) {
  .product-card {
    grid-template-columns: 1fr;
  }
  
  .product-image-section, .product-info-section {
    padding: 1rem;
  }
}
</style>