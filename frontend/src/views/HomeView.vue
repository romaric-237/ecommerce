<script setup>
import CategoryList from '@/components/categorylist.vue'
</script>

<template>
  <main class="min-vh-100 bg-primary-50">
    <!-- Hero Section avec Bootstrap et Tailwind -->
    <div class="hero-section position-relative h-60vh bg-cover bg-center" 
         style="background-image: url('https://images.unsplash.com/photo-1607082348824-0a96f2a4b9da?w=1920&auto=format&fit=crop&q=80')">
      <div class="position-absolute top-0 start-0 w-100 h-100 bg-gradient-to-br from-primary-500/90 to-primary-600/80"></div>
      <div class="container position-relative z-3 h-100 d-flex align-items-center justify-content-center text-center text-white">
        <div class="row">
          <div class="col-12">
            <h1 class="display-4 fw-bold mb-4 text-shadow">Bienvenue sur Notre Boutique</h1>
            <p class="lead opacity-90">Découvrez notre sélection de produits de qualité</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Liste des catégories -->
    <div class="container py-5">
      <CategoryList />
    </div>
  </main>
</template>

<style scoped>
.text-shadow {
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.h-60vh {
  height: 60vh;
}

@media (max-width: 768px) {
  .h-60vh {
    height: 40vh;
  }
  
  .display-4 {
    font-size: 2rem;
  }
  
  .lead {
    font-size: 1rem;
  }
}

@media (max-width: 480px) {
  .h-60vh {
    height: 35vh;
  }
  
  .display-4 {
    font-size: 1.75rem;
  }
}
</style>
