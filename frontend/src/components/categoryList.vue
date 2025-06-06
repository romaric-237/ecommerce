<template>
  <nav class="category-menu">
    <h5>Catégories</h5>
    <ul v-if="categories.length > 0">
      <li>
        <button
            @click="selectCategory(null)"
            :class="{ 'active': selectedCategoryId === null }">
          Tous les produits
        </button>
      </li>
      <li v-for="category in categories" :key="category.id">
        <button
            @click="selectCategory(category.id)"
            :class="{ 'active': selectedCategoryId === category.id }"
        >
          {{ category.nom }}
        </button>
      </li>
    </ul>
    <div v-else-if="isLoading" class="loading-categories">
      Chargement des catégories...
    </div>
    <div v-else class="no-categories">
      Aucune catégorie disponible.
    </div>
  </nav>
</template>

<script>
import categoryService from '@/services/categoryService';

export default {
  name: 'CategoryMenu',
  props: {
    selectedCategoryId: {
      type: Number,
      default: null
    }
  },
  data() {
    return {
      categories: [],
      isLoading: true,
      error: null
    };
  },
  async created() {
    await this.fetchCategories();
  },
  methods: {
    async fetchCategories() {
      this.isLoading = true;
      this.error = null;
      try {
        this.categories = await categoryService.getAllCategories();
      } catch (err) {
        this.error = "Impossible de charger les catégories.";
        console.error(err);
      } finally {
        this.isLoading = false;
      }
    },
    selectCategory(categoryId) {
      // Émet un événement pour informer le parent de la catégorie sélectionnée
      this.$emit('category-selected', categoryId);
    }
  }
};
</script>

<style scoped>
.category-menu {
  width: 200px; /* Largeur du menu latéral */
  padding: 20px;
  background-color: #f8f8f8;
  border-right: 1px solid #eee;
  box-shadow: 2px 0 5px rgba(0,0,0,0.05);
  min-height: calc(100vh - 60px); /* Ajustez si vous avez un header/footer */
  box-sizing: border-box; /* Inclut padding et border dans la largeur */
}

.category-menu h3 {
  margin-top: 0;
  margin-bottom: 20px;
  color: #333;
  text-align: center;
}

.category-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.category-menu li {
  margin-bottom: 10px;
}

.category-menu button {
  display: block;
  width: 100%;
  padding: 10px 15px;
  text-align: left;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  cursor: pointer;
  font-size: 1em;
  color: #555;
  transition: background-color 0.2s ease, color 0.2s ease, border-color 0.2s ease;
}

.category-menu button:hover {
  background-color: #eef;
  color: #007bff;
  border-color: #aaddff;
}

.category-menu button.active {
  background-color: #007bff;
  color: #fff;
  border-color: #007bff;
  font-weight: bold;
}

.loading-categories, .no-categories {
  text-align: center;
  color: #777;
  font-style: italic;
  margin-top: 20px;
}
</style>