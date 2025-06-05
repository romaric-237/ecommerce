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
  <section class="categories-section">
    <div class="container">
      <h1 class="section-title">Explorez Nos Collections</h1>
      <p class="section-subtitle">Découvrez notre sélection de produits par catégorie</p>

      <div v-if="loading" class="loading-container">
        <div class="spinner"></div>
        <p>Chargement des catégories...</p>
      </div>

      <div v-else-if="error" class="error-message">
        <i class="fas fa-exclamation-circle"></i>
        {{ error }}
      </div>

      <div v-else class="categories-grid">
        <RouterLink 
          v-for="category in categories" 
          :key="category.id" 
          :to="'/category/' + category.id"
          class="category-card"
        >
          <div class="category-thumbnail" :style="{ backgroundImage: `url(${getCategoryImage(category.nom)})` }">
            <div class="category-overlay">
              <i :class="getCategoryIcon(category.nom)" class="category-icon"></i>
            </div>
          </div>
          <div class="category-content">
            <h3 class="category-title">{{ category.nom }}</h3>
            <p class="category-description">{{ category.description }}</p>
            <span class="view-products-btn">Voir les produits</span>
          </div>
        </RouterLink>
      </div>
    </div>
  </section>
</template>

<style scoped>
.categories-section {
  padding: 1rem;
  background: transparent;
}

.section-title {
  color: #1f2937;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 0.5rem;
  text-align: center;
}

.section-subtitle {
  color: #6b7280;
  font-size: 1.125rem;
  margin-bottom: 2rem;
  text-align: center;
}

.loading-container {
  text-align: center;
  padding: 2rem;
}

.spinner {
  width: 40px;
  height: 40px;
  margin: 0 auto 1rem;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.error-message {
  text-align: center;
  padding: 2rem;
  color: #dc3545;
  font-size: 1.1rem;
}

.categories-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
  padding: 0.5rem;
}

.category-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
  border: 1px solid rgba(0, 0, 0, 0.05);
  text-decoration: none;
  color: inherit;
  position: relative;
  overflow: hidden;
}

.category-thumbnail {
  height: 200px;
  background-size: cover;
  background-position: center;
  position: relative;
  overflow: hidden;
}

.category-overlay {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(to bottom, rgba(0,0,0,0.2), rgba(0,0,0,0.6));
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all 0.3s ease;
}

.category-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 15px rgba(0, 0, 0, 0.1);
}

.category-card:hover::before {
  opacity: 1;
}

.category-card:hover .category-overlay {
  background: linear-gradient(to bottom, rgba(99,102,241,0.8), rgba(79,70,229,0.9));
}

.category-icon {
  font-size: 3rem;
  color: white;
  text-shadow: 0 2px 4px rgba(0,0,0,0.2);
  transition: all 0.3s ease;
}

.category-card:hover .category-icon {
  transform: scale(1.1);
}

.category-content {
  padding: 1.25rem;
  background: white;
}

.category-title {
  color: #1f2937;
  font-size: 1.25rem;
  font-weight: 600;
  margin-bottom: 0.5rem;
}

.category-description {
  color: #6b7280;
  font-size: 0.875rem;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.view-products-btn {
  background: linear-gradient(135deg, #6366f1 0%, #4f46e5 100%);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 6px;
  text-decoration: none;
  font-size: 0.875rem;
  font-weight: 500;
  transition: all 0.3s ease;
  display: inline-block;
}

.view-products-btn:hover {
  background: linear-gradient(135deg, #4f46e5 0%, #4338ca 100%);
  transform: translateY(-2px);
}

@media (max-width: 768px) {
  .categories-section {
    padding: 0.5rem;
  }

  .section-title {
    font-size: 1.5rem;
    margin-bottom: 0.25rem;
  }

  .section-subtitle {
    font-size: 1rem;
    margin-bottom: 1.5rem;
  }

  .categories-grid {
    grid-template-columns: 1fr;
    gap: 1rem;
    padding: 0;
  }

  .category-thumbnail {
    height: 160px;
  }

  .category-content {
    padding: 1rem;
  }

  .category-title {
    font-size: 1.1rem;
  }

  .category-description {
    font-size: 0.8rem;
    margin-bottom: 0.75rem;
  }

  .view-products-btn {
    padding: 0.4rem 0.8rem;
    font-size: 0.8rem;
  }
}

@media (max-width: 480px) {
  .categories-section {
    padding: 0.25rem;
  }

  .section-title {
    font-size: 1.25rem;
  }

  .section-subtitle {
    font-size: 0.9rem;
    margin-bottom: 1rem;
  }

  .category-thumbnail {
    height: 140px;
  }
}
</style>