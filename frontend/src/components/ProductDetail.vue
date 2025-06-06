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
        <img :src="product.thumbnailUrl || 'https://via.placeholder.com/400x300?text=No+Image'"
             :alt="product.nom" class="product-image" />
      </div>
      <div class="product-info-section">
        <h2 class="product-name">{{ product.nom }}</h2>
        <p class="product-brand">Marque: <strong>{{ product.marque || 'Non spécifiée' }}</strong></p>
        <p class="product-price">Prix: <strong>{{ product.formattedPrice }}</strong></p>

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

export default {
  name: 'ProductDetail',
  props: {
    id: {
      type: [String, Number], // Peut être un string si l'URL le donne ainsi, ou un nombre
      required: true
    }
  },
  data() {
    return {
      product: null,
      isLoading: true,
      error: null
    };
  },
  async created() {
    await this.fetchProduct();
  },
  watch: {
    // Surveille les changements d'ID dans l'URL pour recharger le produit si l'ID change
    id: 'fetchProduct'
  },
  methods: {
    async fetchProduct() {
      this.isLoading = true;
      this.error = null;
      this.product = null; // Réinitialiser le produit précédent
      try {
        const fetchedProduct = await productService.getProductById(this.id);
        // Assurez-vous que productService.getProductById retourne une instance de Product
        this.product = fetchedProduct;
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
      // Logique pour ajouter le produit au panier (peut émettre un événement ou utiliser Vuex)
      console.log(`Produit ${product.nom} ajouté au panier !`);
      alert(`"${product.nom}" a été ajouté au panier !`);
      // Si vous avez un store Vuex, vous appelleriez une action ici:
      // this.$store.dispatch('cart/addProduct', product);
    },
    goBack() {
      // Retourne à la page précédente dans l'historique du navigateur
      this.$router.go(-1);
      // Ou navigue vers une route spécifique, par exemple la liste des produits
      // this.$router.push('/products');
    }
  }
};
</script>

<style scoped>
.product-detail-container {
  padding: 40px;
  max-width: 900px;
  margin: 40px auto;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.loading-message, .error-message, .not-found-message {
  text-align: center;
  font-size: 1.2em;
  color: #777;
  padding: 50px;
}

.error-message {
  color: #dc3545; /* Rouge pour les erreurs */
}

.product-card {
  display: flex;
  flex-wrap: wrap; /* Permet le retour à la ligne sur petits écrans */
  gap: 30px;
  justify-content: center;
}

.product-image-section {
  flex: 1; /* Prend l'espace disponible */
  min-width: 300px; /* Taille minimale */
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.product-image {
  max-width: 100%;
  max-height: 400px; /* Limite la hauteur de l'image */
  object-fit: contain; /* S'assure que l'image est entièrement visible */
}

.product-info-section {
  flex: 1.5; /* Prend plus d'espace */
  min-width: 300px; /* Taille minimale */
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.product-name {
  font-size: 2.2em;
  color: #333;
  margin-bottom: 5px;
  line-height: 1.2;
}

.product-brand, .product-price {
  font-size: 1.3em;
  color: #555;
}

.product-price strong {
  color: #28a745; /* Vert pour le prix */
  font-size: 1.1em;
}

.product-description-section {
  margin-top: 20px;
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.product-description-section h3 {
  font-size: 1.5em;
  color: #444;
  margin-bottom: 10px;
}

.product-description-section p {
  line-height: 1.6;
  color: #666;
  font-size: 1.05em;
}

.product-specs {
  display: flex;
  flex-wrap: wrap;
  gap: 15px 30px;
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px dashed #eee;
}

.product-specs p {
  font-size: 1em;
  color: #777;
}

.product-specs span {
  font-weight: bold;
  color: #333;
}

.action-buttons {
  margin-top: 30px;
  display: flex;
  gap: 15px;
}

.btn-add-to-cart, .btn-go-back {
  padding: 12px 25px;
  border: none;
  border-radius: 5px;
  font-size: 1.1em;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn-add-to-cart {
  background-color: #007bff; /* Bleu primaire */
  color: white;
}

.btn-add-to-cart:hover {
  background-color: #0056b3;
  transform: translateY(-2px);
}

.btn-go-back {
  background-color: #6c757d; /* Gris secondaire */
  color: white;
}

.btn-go-back:hover {
  background-color: #5a6268;
  transform: translateY(-2px);
}

/* Responsive adjustments */
@media (max-width: 768px) {
  .product-card {
    flex-direction: column;
  }

  .product-image-section, .product-info-section {
    min-width: unset;
    width: 100%;
  }

  .product-detail-container {
    padding: 20px;
    margin: 20px auto;
  }
}
</style>