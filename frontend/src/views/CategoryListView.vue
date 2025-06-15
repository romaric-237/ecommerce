<template>
  <div class="category-grid-container">
    <h2>Nos Catégories</h2>

    <div v-if="categories.length === 0 && !isLoading" class="no-categories-message">
      Aucune catégorie disponible pour le moment.
    </div>

    <div v-if="isLoading" class="loading-message">
      Chargement des catégories...
    </div>

    <div v-else class="category-grid">
      <div v-for="category in categories" :key="category.id" class="category-item"
           @click="selectCategory(category.id, category.nom)">
        <div class="thumbnail-wrapper">
          <img :src="getCategoryThumbnail(category)" :alt="category.nom" class="category-thumbnail" />
          <div class="category-name-overlay">{{ category.nom }}</div>
        </div>
        <div class="category-description">
          {{ category.description || 'Description non disponible.' }}
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import categoryService from '@/services/categoryService';

export default {
  name: 'CategoryListView',
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
        const fetchedCategories = await categoryService.getAllCategories();
        this.categories = fetchedCategories;
      } catch (err) {
        this.error = "Impossible de charger les catégories. Veuillez réessayer plus tard.";
        console.error(err);
        this.categories = [];
      } finally {
        this.isLoading = false;
      }
    },
    // Fonction pour obtenir une image de catégorie. Adaptez si vous avez des URLs réelles.
    getCategoryThumbnail(category) {
      // Vous pourriez avoir un champ 'imageUrl' dans votre CategoryEntity/model
      // ou générer une image basée sur le nom de la catégorie, ou juste un placeholder.
      // Pour cet exemple, on utilise un placeholder.
      // return category.imageUrl || `https://via.placeholder.com/150x100?text=${category.nom.substring(0, 10)}`;
      return `https://via.placeholder.com/150x100?text=${encodeURIComponent(category.nom)}`;
    },
    selectCategory(categoryId, categoryName) {
      // Émet un événement si vous voulez que la grille de catégories soit cliquable
      // et redirige vers la liste des produits de cette catégorie, par exemple.
      // Cela peut déclencher une navigation vers /products?categoryId=X
      this.$router.push({ path: '/selected', query: { categoryId: categoryId, categoryName: categoryName } });
    }
  }
};
</script>

<style scoped>
.category-grid-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

h2 {
  text-align: center;
  color: #333;
  margin-bottom: 30px;
}

.no-categories-message, .loading-message {
  text-align: center;
  color: #777;
  font-style: italic;
  margin-top: 50px;
}

.category-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr)); /* Taille des vignettes de catégorie */
  gap: 25px;
  justify-content: center;
}

.category-item {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  transition: transform 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
  cursor: pointer; /* Indique que l'élément est cliquable */
}

.category-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.thumbnail-wrapper {
  position: relative;
  width: 100%;
  height: 120px; /* Hauteur fixe pour l'image de la catégorie */
  display: flex;
  justify-content: center;
  align-items: center;
  overflow: hidden;
  background-color: #f0f0f0; /* Fond gris clair pour les placeholders */
}

.category-thumbnail {
  max-width: 100%;
  max-height: 100%;
  display: block;
  object-fit: contain; /* L'image s'adapte sans être coupée */
}

.category-name-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.6); /* Fond semi-transparent */
  color: white;
  padding: 8px 10px;
  font-weight: bold;
  text-align: center;
  font-size: 1.1em;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.7);
}

.category-description {
  padding: 15px;
  font-size: 0.95em;
  color: #555;
  text-align: center;
  height: 80px; /* Hauteur fixe pour la description */
  overflow: hidden; /* Cache le texte qui dépasse */
  text-overflow: ellipsis; /* Ajoute des points de suspension si le texte est tronqué */
}
</style>