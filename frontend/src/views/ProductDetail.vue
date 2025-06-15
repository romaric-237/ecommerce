<template>
  <div class="product-detail">
    <div v-if="product" class="product-container">
      <div class="product-image">
        <img :src="product.thumbnailUrl" :alt="product.nom" />
      </div>
      <div class="product-info">
        <h1>{{ product.nom }}</h1>
        <p class="description">{{ product.description }}</p>
        <p class="marque">Marque: {{ product.marque }}</p>
        <p class="prix">{{ product.prix }} €</p>
        <p class="genre">Genre: {{ product.genre }}</p>
        <p class="taille">Taille: {{ product.taille }}</p>
        <p class="categorie">Catégorie: {{ product.category?.nom }}</p>
      </div>
    </div>
    <div v-else class="loading">
      Chargement...
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import axios from 'axios'

const route = useRoute()
const product = ref(null)

const fetchProduct = async () => {
  try {
    const response = await axios.get(`http://localhost:8080/api/products/${route.params.id}`)
    product.value = response.data
  } catch (error) {
    console.error('Erreur lors du chargement du produit:', error)
  }
}

onMounted(() => {
  fetchProduct()
})
</script>

<style scoped>
.product-detail {
  padding: 2rem;
  max-width: 1200px;
  margin: 0 auto;
}

.product-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.product-image img {
  width: 100%;
  height: auto;
  border-radius: 8px;
}

.product-info {
  padding: 1rem;
}

.product-info h1 {
  font-size: 2rem;
  margin-bottom: 1rem;
}

.description {
  font-size: 1.1rem;
  line-height: 1.6;
  margin-bottom: 1rem;
}

.prix {
  font-size: 1.5rem;
  font-weight: bold;
  color: #2c3e50;
  margin: 1rem 0;
}

.marque, .genre, .taille, .categorie {
  margin: 0.5rem 0;
  color: #666;
}

.loading {
  text-align: center;
  padding: 2rem;
  font-size: 1.2rem;
  color: #666;
}

@media (max-width: 768px) {
  .product-container {
    grid-template-columns: 1fr;
  }
}
</style> 