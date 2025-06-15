<template>
  <main class="min-vh-100 bg-primary-50">
    <!-- En-tête de la catégorie -->
    <div class="bg-white shadow-sm">
      <div class="container py-4">
        <div class="d-flex align-items-center">
          <RouterLink to="/" class="btn btn-outline-primary me-3">
            <i class="fas fa-arrow-left me-2"></i>Retour
          </RouterLink>
          <div>
            <h1 class="h3 mb-1">{{ category?.nom }}</h1>
            <p class="text-muted mb-0">{{ category?.description }}</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Liste des produits -->
    <div class="container py-5">
      <div v-if="loading" class="d-flex flex-column align-items-center justify-content-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Chargement...</span>
        </div>
        <p class="mt-3 text-muted">Chargement des produits...</p>
      </div>

      <div v-else-if="error" class="alert alert-danger text-center" role="alert">
        <i class="fas fa-exclamation-circle me-2"></i>
        {{ error }}
      </div>

      <div v-else-if="products.length === 0" class="text-center py-5">
        <i class="fas fa-box-open text-muted mb-3" style="font-size: 3rem;"></i>
        <h3 class="h4 text-muted">Aucun produit disponible</h3>
        <p class="text-muted">Cette catégorie ne contient pas encore de produits.</p>
      </div>

      <div v-else class="row g-4">
        <div v-for="product in products" 
             :key="product.id" 
             class="col-12 col-md-6 col-lg-4">
          <div class="card card-hover h-100">
            <div class="position-relative">
              <img :src="product.image" 
                   :alt="product.nom" 
                   class="card-img-top"
                   style="height: 200px; object-fit: cover;">
              <div class="position-absolute top-0 end-0 p-2">
                <span class="badge bg-primary">{{ product.prix }} €</span>
              </div>
            </div>
            <div class="card-body">
              <h3 class="h5 fw-bold mb-2" 
                  data-bs-toggle="tooltip" 
                  data-bs-placement="top" 
                  :title="product.nom">{{ product.nom }}</h3>
              <p class="text-muted small mb-3">{{ product.description }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'
import { Tooltip } from 'bootstrap'

const route = useRoute()
const category = ref(null)
const products = ref([])
const loading = ref(true)
const error = ref(null)

const fetchCategory = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/categories/${route.params.id}`)
    category.value = response.data
  } catch (err) {
    error.value = "Erreur lors du chargement de la catégorie"
  }
}

const fetchProducts = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/products/category/${route.params.id}`)
    products.value = response.data
    // Initialiser les tooltips après le chargement des produits
    setTimeout(() => {
      const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
      tooltipTriggerList.forEach(tooltipTriggerEl => new Tooltip(tooltipTriggerEl))
    }, 100)
  } catch (err) {
    error.value = "Erreur lors du chargement des produits"
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  await Promise.all([fetchCategory(), fetchProducts()])
})
</script>

<style scoped>
.card-hover {
  transition: all 0.3s ease;
}

.card-hover:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0,0,0,0.1);
}

.badge {
  font-size: 0.9rem;
  padding: 0.5em 1em;
}

@media (max-width: 768px) {
  .card-img-top {
    height: 180px !important;
  }
}

@media (max-width: 480px) {
  .card-img-top {
    height: 160px !important;
  }
}
</style> 