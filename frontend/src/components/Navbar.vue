<template>
  <nav class="app-navbar">
    <div class="navbar-content">
      <router-link to="/" class="navbar-brand-custom">Ecommerce</router-link>

      <div class="navbar-links">
        <ul class="nav-list">
          <li class="nav-item-custom">
            <router-link to="/products"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'produits' }"
                         @click="setActiveTab('produits')">Produits</router-link>
          </li>
          <li class="nav-item-custom">
            <router-link to="/categories"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'categories' }"
                         @click="setActiveTab('categories')">Catégories</router-link>
          </li>
          <li class="nav-item-custom">
            <router-link to="/register"
                         class="nav-link-custom"
                         :class="{ 'active-link': activeTab === 'inscription' }"
                         @click="setActiveTab('inscription')">Inscription</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

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
      } else if (tab === 'inscription') {
        this.$router.push('/register');
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
        } else if (newPath.includes('/register')) {
          this.activeTab = 'inscription';
        } else {
          this.activeTab = 'produits';
        }
      }
    }
  }
};
</script>

<style scoped>
:root {
  --navbar-bg-color: #333;
  --navbar-text-color: #f8f8f8;
  --navbar-hover-color: #42b983;
  --navbar-active-color: #007bff;
  --navbar-brand-color: #fff;
}

.app-navbar {
  background-color: var(--navbar-bg-color);
  padding: 1rem 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  color: var(--navbar-text-color);
}

.navbar-content {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  max-width: 1200px;
  margin: 0 auto;
}

.navbar-brand-custom {
  color: var(--navbar-brand-color);
  font-size: 1.8rem;
  font-weight: bold;
  text-decoration: none;
  transition: color 0.3s ease;
}

.navbar-brand-custom:hover {
  color: var(--navbar-hover-color);
}

.nav-list {
  display: flex;
  list-style: none;
  margin: 0;
  padding: 0;
}

.nav-item-custom {
  margin-left: 15px;
}

.nav-list .nav-item-custom:first-child {
  margin-left: 50px;
}

.nav-link-custom {
  color: var(--navbar-text-color);
  text-decoration: none;
  font-size: 1.1rem;
  padding: 8px 0;
  position: relative;
  transition: color 0.3s ease;
}

.nav-link-custom:hover {
  color: var(--navbar-hover-color);
}

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

.nav-link-custom.active-link {
  color: var(--navbar-active-color);
  font-weight: bold;
}

.nav-link-custom.active-link::after {
  width: 100%;
  background-color: var(--navbar-active-color);
}

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
