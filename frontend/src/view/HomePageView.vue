<template>
  <div class="home-page-container">
    <header class="app-header">
      <h4>Bienvenue sur notre boutique !</h4>
    </header>

    <div class="main-content">
      <CategoryList
          :selectedCategoryId="selectedCategoryId"
          @category-selected="handleCategorySelected"/>

      <ProductList
          :is-title-center="true"
          :categoryId="selectedCategoryId"
          :categoryName="selectedCategoryName"/>
    </div>
  </div>
</template>

<script>
import CategoryList from '@/components/categoryList.vue';
import ProductList from '@/components/productList.vue';
import categoryService from '@/services/categoryService.js';

export default {
  name: 'HomePage',
  components: {
    CategoryList,
    ProductList
  },
  data() {
    return {
      selectedCategoryId: null,
      categories: [],
      selectedCategoryName: 'Tous les produits'
    };
  },
  async created() {
    // Charger toutes les catégories au démarrage pour pouvoir trouver leur nom
    try {
      this.categories = await categoryService.getAllCategories();
    } catch (error) {
      console.error("Erreur lors du chargement des catégories pour le nom :", error);
    }
  },
  methods: {
    handleCategorySelected(categoryId) {
      this.selectedCategoryId = categoryId;
      // Mettre à jour le nom de la catégorie pour le titre de ProductList
      if (categoryId === null) {
        this.selectedCategoryName = 'Tous les produits';
      } else {
        const category = this.categories.find(cat => cat.id === categoryId);
        this.selectedCategoryName = category ? category.nom : 'Catégorie inconnue';
      }
    }
  }
};
</script>

<style scoped>
.home-page-container {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.app-header {
  //background-color: #42b983; /* Couleur d'en-tête */
  //color: white;
  color: #b86161;
  padding: 15px 0;
  text-align: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.app-header h1 {
  margin: 0;
  font-size: 2em;
}

.main-content {
  display: flex; /* Utilise Flexbox pour aligner le menu et la liste de produits */
  flex-grow: 1; /* Permet au contenu principal de prendre l'espace restant */
}
</style>