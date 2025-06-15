<template>
  <div class="home">
    <div class="hero-section">
      <div class="hero-content">
        <h1>Bienvenue sur notre boutique</h1>
        <p>Découvrez notre sélection de produits de qualité</p>
      </div>
    </div>

    <div class="container mt-5">
      <div class="row">
        <div class="col-12">
          <h2 class="text-center mb-4">Nos Catégories</h2>
        </div>
      </div>
      <CategoryList />
    </div>
  </div>
</template>

<script>
import CategoryList from '@/components/CategoryList.vue'

export default {
  name: 'HomeView',
  components: {
    CategoryList
  }
}
</script>

<style scoped>
.hero-section {
  background-image: url('@/assets/hero-bg.jpg');
  background-size: cover;
  background-position: center;
  height: 60vh;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  color: white;
  text-align: center;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5));
}

.hero-content {
  position: relative;
  z-index: 1;
  padding: 2rem;
}

.hero-content h1 {
  font-size: 3rem;
  margin-bottom: 1rem;
  font-weight: bold;
}

.hero-content p {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}

@media (max-width: 768px) {
  .hero-section {
    height: 40vh;
  }

  .hero-content h1 {
    font-size: 2rem;
  }

  .hero-content p {
    font-size: 1.2rem;
  }
}

@media (max-width: 480px) {
  .hero-section {
    height: 35vh;
  }

  .hero-content h1 {
    font-size: 1.8rem;
  }

  .hero-content p {
    font-size: 1rem;
  }
}
</style> 