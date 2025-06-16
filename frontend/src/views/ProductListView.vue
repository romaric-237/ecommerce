<script>
import ProductList from "@/components/productList.vue";

export default {
  name: "ProductListView",
  components: {
    ProductList
  },
  data (){
    return {
      selectedCategoryId: null,
      selectedCategoryName:"Tous les produits"
    }
  }
}
</script>

<template>
  <ProductList
      :is-title-center="false"
      :categoryId="selectedCategoryId"
      :categoryName="selectedCategoryName"/>
</template>

<style scoped>

</style>