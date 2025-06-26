<script>
import ProductList from "@/components/ProductList.vue";
import authService from '@/services/authService';

export default {
  name: "ProductListView",
  components: {
    ProductList
  },
  data() {
    return {
      categoryId: null,
      categoryName: 'Tous les produits',
      isGestionnaire: false
    };
  },
  created() {
    // Vérifier si l'utilisateur est un gestionnaire
    this.isGestionnaire = authService.isGestionnaire();
    
    // Récupérer les paramètres de route
    if (this.$route.params.categoryId) {
      this.categoryId = parseInt(this.$route.params.categoryId);
    }
    if (this.$route.params.categoryName) {
      this.categoryName = this.$route.params.categoryName;
    }
  },
  watch: {
    '$route.params': {
      handler(newParams) {
        if (newParams.categoryId) {
          this.categoryId = parseInt(newParams.categoryId);
        } else {
          this.categoryId = null;
        }
        if (newParams.categoryName) {
          this.categoryName = newParams.categoryName;
        } else {
          this.categoryName = 'Tous les produits';
        }
      },
      immediate: true
    }
  }
};
</script>

<template>
  <div class="product-list-view">
    <ProductList 
      :categoryId="categoryId" 
      :categoryName="categoryName"
      :isGestionnaire="isGestionnaire"
    />
  </div>
</template>

<style scoped>

</style>