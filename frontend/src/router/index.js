import { createRouter, createWebHistory } from 'vue-router'
import ProductListView from '@/views/ProductListView.vue'
import CategoryListView from '@/views/CategoryListView.vue'
import HomePage from "@/views/HomePageView.vue";
import ProductDetail from "../components/ProductDetail.vue";
import HomeView from '../views/HomePageView.vue'
import Register from '../components/Register.vue'
import Login from '../components/Login.vue'
import ProductList from '../components/ProductList.vue'
import ProfileView from '../views/ProfileView.vue'
import AdminProductManagement from '../views/AdminProductManagement.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
      meta: { requiresAuth: false }
    },
    {
      path: '/selected',
      name: 'category-selected',
      component: HomePage,
      meta: { requiresAuth: false },
      props: (route) => ({
        categoryId: route.query.categoryId ? parseInt(route.query.categoryId) : null,
        categoryName: route.query.categoryName || 'Tous les produits'
      })
    },
    {
      path: '/products',
      name: 'product-list',
      component: ProductListView,
      meta: { requiresAuth: false }
    },
    {
      path: '/categories',
      name: 'category-list',
      component: CategoryListView,
      meta: { requiresAuth: false }
    },
    {
      path: '/product/:id',
      name: 'product-detail',
      component: ProductDetail,
      props: true,
      meta: { requiresAuth: false }
    },
    // {
    //   path: '/privacy-policy',
    //   name: 'privacy-policy',
    //   component: PrivacyPolicy
    // },
    {
      path: '/register',
      name: 'register',
      component: Register,
      meta: { requiresAuth: false }
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      meta: { requiresAuth: false }
    },
    // {
    //   path: '/categories',
    //   name: 'categories',
    //   component: Category
    // },
    {
      path: '/profile',
      name: 'profile',
      component: ProfileView,
      meta: { requiresAuth: true }
    },
    {
      path: '/products-list',
      name: 'products-list',
      component: ProductList,
      meta: { requiresAuth: false }
    },
    {
      path: '/admin/products',
      name: 'admin-products',
      component: AdminProductManagement,
      meta: { 
        requiresAuth: true,
        requiresRole: 'GESTIONNAIRE'
      }
    },
    // {
    //   path: '/category/:id',
    //   name: 'category-detail',
    //   component: CategoryProducts
    // }
  ],
})

// Import du service d'authentification
import authService from '../services/authService.js';

// Navigation guard pour protéger les routes
router.beforeEach(async (to, from, next) => {
  const isAuthenticated = authService.isAuthenticated();
  const requiresAuth = to.meta.requiresAuth !== false; // Par défaut, l'auth est requise
  const requiredRole = to.meta.requiresRole;
  
  // Si la route nécessite une authentification
  if (requiresAuth && !isAuthenticated) {
    console.log('Redirection vers login - utilisateur non authentifié');
    next('/login');
    return;
  }
  
  // Si l'utilisateur est connecté et essaie d'accéder aux pages de login/register
  if (isAuthenticated && (to.path === '/login' || to.path === '/register')) {
    console.log('Redirection vers accueil - utilisateur déjà connecté');
    next('/');
    return;
  }
  
  // Validation additionnelle du token pour les routes protégées
  if (requiresAuth && isAuthenticated) {
    try {
      const isValid = await authService.validateToken();
      if (!isValid) {
        console.log('Token invalide, redirection vers login');
        authService.logout();
        next('/login');
        return;
      }
      
      // Vérification du rôle si requis
      if (requiredRole) {
        const userRole = authService.getUserRole();
        if (userRole !== requiredRole) {
          console.log(`Accès refusé - rôle requis: ${requiredRole}, rôle actuel: ${userRole}`);
          next('/profile'); // Redirection vers le profil
          return;
        }
      }
    } catch (error) {
      console.error('Erreur lors de la validation du token:', error);
      authService.logout();
      next('/login');
      return;
    }
  }
  
  next();
});

export default router
