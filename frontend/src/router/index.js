import { createRouter, createWebHistory } from 'vue-router'
//import ProductDetail from '../components/ProductDetail.vue'
//import CategoryProducts from '../components/CategoryProducts.vue'
import ProductListView from '../view/ProductListView.vue'
import CategoryListView from '../view/CategoryListView.vue'
import HomePage from "../view/HomePageView.vue";
import ProductDetail from "@/components/ProductDetail.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomePage,
    },
    {
      path: '/selected',
      name: 'category-selected',
      component: HomePage,
      props: (route) => ({
        categoryId: route.query.categoryId ? parseInt(route.query.categoryId) : null,
        categoryName: route.query.categoryName || 'Tous les produits'
      })
    },
    {
      path: '/products',
      name: 'product-list',
      component: ProductListView,
    },
    {
      path: '/categories',
      name: 'category-list',
      component: CategoryListView,
    },
    {
      path: '/product/:id',
      name: 'product-detail',
      component: ProductDetail,
      props: true // Permet de passer l'ID de la route directement comme prop au composant
    }
    // {
    //   path: '/category/:id',
    //   name: 'category-products',
    //   component: CategoryProducts
    // }
  ],
})

export default router
