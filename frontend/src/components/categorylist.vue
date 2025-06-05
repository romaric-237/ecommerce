<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

interface Category {
  id: number
  nom: string
  description: string
}

const categories = ref<Category[]>([])
const loading = ref(true)
const error = ref('')

const fetchCategories = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/categories')
    categories.value = response.data
    loading.value = false
  } catch (e) {
    error.value = 'Erreur lors du chargement des catégories'
    loading.value = false
  }
}

onMounted(() => {
  fetchCategories()
})

const getCategoryIcon = (categoryName) => {
  const icons = {
    'Electronics': 'fas fa-laptop',
    'Clothing': 'fas fa-tshirt',
    'Home': 'fas fa-home',
    'Sports': 'fas fa-running',
    'Beauty': 'fas fa-spa',
    'Toys': 'fas fa-gamepad',
    'Books': 'fas fa-book',
    'Food': 'fas fa-utensils'
  }
  return icons[categoryName] || 'fas fa-tag'
}

const getCategoryImage = (categoryName) => {
  const images = {
    'Electronics': 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=500&auto=format&fit=crop&q=60',
    'Clothing': 'https://images.unsplash.com/photo-1445205170230-053b83016050?w=500&auto=format&fit=crop&q=60',
    'Home': 'https://images.unsplash.com/photo-1484101403633-562f891dc89a?w=500&auto=format&fit=crop&q=60',
    'Sports': 'https://images.unsplash.com/photo-1461896836934-ffe607ba8211?w=500&auto=format&fit=crop&q=60',
    'Beauty': 'https://images.unsplash.com/photo-1522335789203-aabd1fc54bc9?w=500&auto=format&fit=crop&q=60',
    'Toys': 'https://images.unsplash.com/photo-1566576912321-d58ddd7a6088?w=500&auto=format&fit=crop&q=60',
    'Books': 'https://images.unsplash.com/photo-1495446815901-a7297e633e8d?w=500&auto=format&fit=crop&q=60',
    'Food': 'https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=500&auto=format&fit=crop&q=60'
  }
  return images[categoryName] || 'https://images.unsplash.com/photo-1498049794561-7780e7231661?w=500&auto=format&fit=crop&q=60'
}
</script>

<template>
  <section class="py-4">
    <div class="container">
      <h1 class="display-5 text-center text-dark mb-2">Explorez Nos Collections</h1>
      <p class="lead text-center text-muted mb-5">Découvrez notre sélection de produits par catégorie</p>

      <!-- Loading State -->
      <div v-if="loading" class="d-flex flex-column align-items-center justify-content-center py-5">
        <div class="spinner-border text-primary" role="status">
          <span class="visually-hidden">Chargement...</span>
        </div>
        <p class="mt-3 text-muted">Chargement des catégories...</p>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="alert alert-danger text-center" role="alert">
        <i class="fas fa-exclamation-circle me-2"></i>
        {{ error }}
      </div>

      <!-- Categories Grid -->
      <div v-else class="row g-4">
        <div v-for="category in categories" 
             :key="category.id" 
             class="col-12 col-md-6 col-lg-4">
          <RouterLink 
            :to="'/category/' + category.id"
            class="card card-hover text-decoration-none h-100"
          >
            <div class="position-relative h-48 bg-cover bg-center" 
                 :style="{ backgroundImage: `url(${getCategoryImage(category.nom)})` }">
              <div class="position-absolute top-0 start-0 w-100 h-100 bg-gradient-to-b from-black/20 to-black/60 d-flex align-items-center justify-content-center">
                <i :class="[getCategoryIcon(category.nom), 'text-4xl text-white']"></i>
              </div>
            </div>
            <div class="card-body p-4">
              <h3 class="h5 fw-bold text-dark mb-2">{{ category.nom }}</h3>
              <p class="text-muted small mb-3 line-clamp-2">{{ category.description }}</p>
              <button class="btn btn-primary w-100">Voir les produits</button>
            </div>
          </RouterLink>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.line-clamp-2 {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.h-48 {
  height: 12rem;
}

@media (max-width: 768px) {
  .h-48 {
    height: 10rem;
  }
}
</style>