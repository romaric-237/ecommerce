<script setup lang="ts">
import { ref, onMounted } from 'vue'
import axios from 'axios'

interface Product {
  id: number
  nom: string
  description: string
  marque: string
  prix: number
  thumbnailUrl: string
  genre: string
  taille: string
  category: {
    id: number
    nom: string
  }
}

const products = ref<Product[]>([])
const loading = ref(true)
const error = ref('')

const fetchProducts = async () => {
  try {
    const response = await axios.get('http://localhost:8080/api/products')
    products.value = response.data
    loading.value = false
  } catch (e) {
    error.value = 'Erreur lors du chargement des produits'
    loading.value = false
  }
}

onMounted(() => {
  fetchProducts()
})
</script>

<template>
  <div class="product-list">
    <div v-if="loading" class="loading">
      Chargement des produits...
    </div>
    
    <div v-else-if="error" class="error">
      {{ error }}
    </div>
    
    <div v-else class="products-grid">
      <div v-for="product in products" :key="product.id" class="product-card">
        <router-link :to="'/product/' + product.id" class="product-link">
          <div class="product-image">
            <img :src="product.thumbnailUrl" :alt="product.nom">
          </div>
          <div class="product-info">
            <h3>{{ product.nom }}</h3>
            <p class="price">{{ product.prix }} €</p>
            <p class="brand">{{ product.marque }}</p>
          </div>
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.product-list {
  padding: 2rem;
}

.loading, .error {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
}

.error {
  color: #dc3545;
}

.products-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 2rem;
  padding: 1rem;
}

.product-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.2s, box-shadow 0.2s;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.product-link {
  text-decoration: none;
  color: inherit;
}

.product-image {
  width: 100%;
  height: 200px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-info {
  padding: 1rem;
}

.product-info h3 {
  margin: 0 0 0.5rem 0;
  font-size: 1.1rem;
  color: #2c3e50;
}

.price {
  font-weight: bold;
  color: #2c3e50;
  margin: 0.5rem 0;
}

.brand {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}
</style>