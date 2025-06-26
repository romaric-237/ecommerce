<template>
  <div class="admin-product-management">
    <div class="container mt-4">
      <h1 class="text-center mb-4">
        <i class="fas fa-cogs me-2"></i>
        Gestion des Produits
      </h1>

      <!-- Messages d'alerte -->
      <div v-if="alertMessage" class="alert alert-dismissible fade show" :class="alertClass">
        <i :class="alertIcon"></i>
        {{ alertMessage }}
        <button type="button" class="btn-close" @click="clearAlert"></button>
      </div>

      <!-- Liste des produits -->
      <div class="card">
        <div class="card-header d-flex justify-content-between align-items-center">
          <h5 class="mb-0">Liste des Produits</h5>
          <button class="btn btn-primary" @click="loadProducts" :disabled="loading">
            <i class="fas fa-sync-alt me-1"></i>
            Actualiser
          </button>
        </div>
        <div class="card-body">
          <div v-if="loading" class="text-center py-4">
            <div class="spinner-border text-primary" role="status"></div>
            <p class="mt-2">Chargement...</p>
          </div>
          
          <div v-else-if="products.length > 0" class="table-responsive">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Image</th>
                  <th>Nom</th>
                  <th>Marque</th>
                  <th>Prix</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="product in products" :key="product.id">
                  <td>{{ product.id }}</td>
                  <td>
                    <img :src="product.thumbnailUrl" :alt="product.nom" class="product-thumbnail">
                  </td>
                  <td>{{ product.nom }}</td>
                  <td>{{ product.marque }}</td>
                  <td>{{ formatPrice(product.prix) }}</td>
                  <td>
                    <button class="btn btn-outline-primary btn-sm me-2" @click="editProduct(product)">
                      <i class="fas fa-edit"></i> Modifier
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          
          <div v-else class="text-center py-4">
            <p class="text-muted">Aucun produit trouvé</p>
          </div>
        </div>
      </div>

      <!-- Formulaire de modification -->
      <div v-if="showForm" class="card mt-4">
        <div class="card-header">
          <h5 class="mb-0">Modifier le Produit</h5>
        </div>
        <div class="card-body">
          <ProductEditForm 
            :product="selectedProduct"
            :categories="categories"
            @submit="handleSubmit"
            @cancel="hideForm"
            :loading="formLoading"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import productService from '@/services/productService.js';
import categoryService from '@/services/categoryService.js';
import ProductEditForm from '@/components/ProductEditForm.vue';

export default {
  name: 'AdminProductManagement',
  components: {
    ProductEditForm
  },
  data() {
    return {
      products: [],
      categories: [],
      selectedProduct: null,
      showForm: false,
      loading: false,
      formLoading: false,
      alertMessage: '',
      alertType: 'success'
    };
  },
  computed: {
    alertClass() {
      return `alert-${this.alertType}`;
    },
    alertIcon() {
      return this.alertType === 'success' ? 'fas fa-check-circle' : 'fas fa-exclamation-triangle';
    }
  },
  async mounted() {
    await this.loadProducts();
    await this.loadCategories();
  },
  methods: {
    async loadProducts() {
      this.loading = true;
      try {
        this.products = await productService.getAllProductsAdmin();
        this.showSuccess('Produits chargés avec succès');
      } catch (error) {
        this.showError(error.message);
      } finally {
        this.loading = false;
      }
    },

    async loadCategories() {
      try {
        this.categories = await categoryService.getAllCategories();
      } catch (error) {
        console.error('Erreur lors du chargement des catégories:', error);
      }
    },

    editProduct(product) {
      this.selectedProduct = { ...product };
      this.showForm = true;
    },

    hideForm() {
      this.showForm = false;
      this.selectedProduct = null;
    },

    async handleSubmit(productData) {
      this.formLoading = true;
      try {
        await productService.updateProduct(this.selectedProduct.id, productData);
        this.showSuccess('Produit mis à jour avec succès');
        this.hideForm();
        await this.loadProducts();
      } catch (error) {
        this.showError(error.message);
      } finally {
        this.formLoading = false;
      }
    },

    formatPrice(price) {
      return new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR'
      }).format(price);
    },

    showSuccess(message) {
      this.alertMessage = message;
      this.alertType = 'success';
      setTimeout(() => this.clearAlert(), 5000);
    },

    showError(message) {
      this.alertMessage = message;
      this.alertType = 'danger';
      setTimeout(() => this.clearAlert(), 8000);
    },

    clearAlert() {
      this.alertMessage = '';
    }
  }
};
</script>

<style scoped>
.product-thumbnail {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}
</style> 