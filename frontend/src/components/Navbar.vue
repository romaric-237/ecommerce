<script>
export default {
  name: "Navbar",

  data() {
    return {
      activeTab: "produits",
    };
  },
  methods: {
    setActiveTab(tab) {
      this.activeTab = tab;
      if (tab === 'produits') {
        this.$router.push('/products');
      } else if (tab === 'categories') {
        this.$router.push('/categories');
      }
      this.$emit("tab-changed", tab);
    },
  },
  watch: {
    '$route.path': {
      immediate: true,
      handler(newPath) {
        if (newPath.includes('/products')) {
          this.activeTab = 'produits';
        } else if (newPath.includes('/categories')) {
          this.activeTab = 'categories';
        } else {
          this.activeTab = 'produits';
        }
      }
    }
  }
};
</script>

<template>
  <nav class="bg-white shadow">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-16">
        <div class="flex">
          <!-- Logo -->
          <div class="flex-shrink-0 flex items-center">
            <router-link to="/" class="text-xl font-bold text-indigo-600">
              E-Commerce
            </router-link>
          </div>

          <!-- Navigation Links -->
          <div class="hidden sm:ml-6 sm:flex sm:space-x-8">
            <router-link
              to="/"
              class="border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700 inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium"
            >
              Accueil
            </router-link>
            <router-link to="/products"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'produits' }"
                         @click="setActiveTab('produits')">Produits</router-link>
            <router-link to="/categories"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'categories' }"
                         @click="setActiveTab('categories')">Catégories</router-link>
          </div>
        </div>

        <!-- Right side -->
        <div class="hidden sm:ml-6 sm:flex sm:items-center">
          <router-link
            to="/register"
            class="text-gray-500 hover:text-gray-700 px-3 py-2 rounded-md text-sm font-medium"
          >
            S'inscrire
          </router-link>
          <router-link
            to="/login"
            class="ml-4 text-gray-500 hover:text-gray-700 px-3 py-2 rounded-md text-sm font-medium"
          >
            Se connecter
          </router-link>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>
/* Couleurs personnalisées */
:root {
  --navbar-bg-color: #333; /* Gris foncé élégant */
  --navbar-text-color: #f8f8f8; /* Blanc cassé */
  --navbar-hover-color: #42b983; /* Vert de Vue.js pour le survol */
  --navbar-active-color: #007bff; /* Bleu vif pour le lien actif */
  --navbar-brand-color: #fff; /* Blanc pur pour la marque */
}

.app-navbar {
  background-color: var(--navbar-bg-color);
  padding: 1rem 2rem; /* Ajustez le padding */
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2); /* Ombre douce */
  color: var(--navbar-text-color);
}

.navbar-content {
  display: flex;
  justify-content: flex-start; /* Espacement entre le logo et les liens */
  align-items: center;
  max-width: 1200px; /* Limite la largeur du contenu de la barre de navigation */
  margin: 0 auto; /* Centre le contenu */
}

.navbar-brand-custom {
  color: var(--navbar-brand-color);
  font-size: 1.8rem; /* Taille du logo */
  font-weight: bold;
  text-decoration: none; /* Pas de soulignement */
  transition: color 0.3s ease;
}

.navbar-brand-custom:hover {
  color: var(--navbar-hover-color);
}

.nav-list {
  display: flex; /* Affiche les éléments de liste en ligne */
  list-style: none; /* Supprime les puces */
  margin: 0;
  padding: 0;
}

.nav-item-custom {
  margin-left: 15px; /* Espacement entre les éléments de navigation */
}
.nav-list .nav-item-custom:first-child {
  margin-left: 50px;
}
.nav-link-custom {
  color: var(--navbar-text-color);
  text-decoration: none;
  font-size: 1.1rem;
  padding: 8px 0; /* Padding vertical pour le lien */
  position: relative; /* Pour l'effet de soulignement */
  transition: color 0.3s ease;
}

.nav-link-custom:hover {
  color: var(--navbar-hover-color);
}

/* Effet de soulignement moderne au survol */
.nav-link-custom::after {
  content: '';
  position: absolute;
  width: 0;
  height: 2px;
  bottom: 0;
  left: 0;
  background-color: var(--navbar-hover-color);
  transition: width 0.3s ease;
}

.nav-link-custom:hover::after {
  width: 100%;
}

/* Style du lien actif */
.nav-link-custom.active-link {
  color: var(--navbar-active-color); /* Couleur différente pour le lien actif */
  font-weight: bold;
}

.nav-link-custom.active-link::after {
  width: 100%; /* Le lien actif est souligné en permanence */
  background-color: var(--navbar-active-color);
}

/* Media queries pour la responsivité (menu hamburger) */
@media (max-width: 768px) {
  .navbar-content {
    flex-direction: column;
    align-items: flex-start;
  }

  .navbar-brand-custom {
    margin-bottom: 15px;
  }

  .navbar-links {
    width: 100%;
  }

  .nav-list {
    flex-direction: column;
    width: 100%;
  }

  .nav-item-custom {
    margin-left: 0;
    margin-bottom: 10px;
    width: 100%;
  }

  .nav-link-custom {
    padding: 10px 15px;
    text-align: center;
    border-radius: 5px;
    background-color: rgba(255, 255, 255, 0.1);
  }

  .nav-link-custom::after {
    display: none;
  }

  .nav-link-custom.active-link {
    background-color: var(--navbar-active-color);
    color: white;
  }
}
</style>