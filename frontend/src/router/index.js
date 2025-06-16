import { createRouter, createWebHistory } from 'vue-router'
//import ProductDetail from '../components/ProductDetail.vue'
//import CategoryProducts from '../components/CategoryProducts.vue'
import ProductListView from '../views/ProductListView.vue'
import CategoryListView from '../views/CategoryListView.vue'
import HomePage from "../views/HomePageView.vue";
import ProductDetail from "@/components/ProductDetail.vue";
import HomeView from '../views/HomeView.vue'
import PrivacyPolicy from '../views/PrivacyPolicy.vue'
import RegisterView from '../views/RegisterView.vue';
import LoginView from '../views/LoginView.vue';
import ProfilView from '../views/ProfilView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
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
    },
    {
      path: '/privacy-policy',
      name: 'privacy-policy',
      component: PrivacyPolicy
    },
    { path: "/register", 
      name: "Register", 
      component: RegisterView 
    },
    { path: '/login',
      name: 'Login',
      component: LoginView 
    },
    { path: '/profil',
      name: 'Profil',
      component: ProfilView 
    }

    // {
    //   path: '/category/:id',
    //   name: 'category-products',
    //   component: CategoryProducts
    // }
  ],
})

export default router
