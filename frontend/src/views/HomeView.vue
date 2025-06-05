<script setup>
import CategoryList from '@/components/categorylist.vue'
</script>

<template>
  <main class="home">
    <div class="hero-section">
      <div class="hero-content">
        <h1>Bienvenue sur Notre Boutique</h1>
        <p>Découvrez notre sélection de produits de qualité</p>
      </div>
    </div>
    <CategoryList />
  </main>
</template>

<style scoped>
.home {
  min-height: 100vh;
  background: #f5f7fa;
}

.hero-section {
  height: 60vh;
  background-image: url('https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1920&auto=format&fit=crop&q=80');
  background-size: cover;
  background-position: center;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: white;
  margin-bottom: 2rem;
}

.hero-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(99,102,241,0.9), rgba(79,70,229,0.8));
}

.hero-content {
  position: relative;
  z-index: 1;
  max-width: 800px;
  padding: 0 1rem;
}

.hero-content h1 {
  font-size: 3.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.hero-content p {
  font-size: 1.25rem;
  opacity: 0.9;
}

@media (max-width: 768px) {
  .hero-section {
    height: 40vh;
    margin-bottom: 1rem;
  }

  .hero-content {
    padding: 0 1.5rem;
  }

  .hero-content h1 {
    font-size: 2rem;
    margin-bottom: 0.5rem;
  }

  .hero-content p {
    font-size: 1rem;
    line-height: 1.4;
  }
}

@media (max-width: 480px) {
  .hero-section {
    height: 35vh;
  }

  .hero-content h1 {
    font-size: 1.75rem;
  }

  .hero-content p {
    font-size: 0.9rem;
  }
}
</style>
