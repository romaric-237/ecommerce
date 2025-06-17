# Documentation Frontend E-Commerce

## Vue d'ensemble
Le frontend de l'application e-commerce est développé avec Vue.js 3, utilisant Vite comme outil de build. L'interface combine Bootstrap 5 et Tailwind CSS pour un design moderne et responsive.

## Structure du Projet

```
frontend/
├── src/
│   ├── components/          # Composants réutilisables
│   │   ├── CategoryList.vue # Liste des catégories
│   │   ├── ProductCard.vue  # Carte produit
│   │   └── ProductDetail.vue # Détails produit
│   ├── views/              # Pages principales
│   │   ├── HomeView.vue    # Page d'accueil
│   │   └── CategoryProducts.vue # Produits par catégorie
│   ├── services/           # Services API
│   │   ├── productService.js
│   │   └── categoryService.js
│   ├── router/             # Configuration des routes
│   │   └── index.js
│   └── assets/            # Ressources statiques
└── public/               # Fichiers publics
```

## Composants Principaux

### CategoryList
Affiche la liste des catégories disponibles.

```vue
<template>
  <div class="category-list">
    <div v-for="category in categories" :key="category.id">
      <!-- Contenu de la catégorie -->
    </div>
  </div>
</template>
```

### ProductCard
Affiche un produit individuel dans une carte.

```vue
<template>
  <div class="product-card">
    <img :src="product.image" :alt="product.nom">
    <h3>{{ product.nom }}</h3>
    <p class="price">{{ product.prix }} €</p>
  </div>
</template>
```

### ProductDetail
Affiche les détails complets d'un produit.

```vue
<template>
  <div class="product-detail">
    <div class="product-image">
      <img :src="displayedImageUrl" :alt="product.nom">
    </div>
    <div class="product-info">
      <h2>{{ product.nom }}</h2>
      <p class="price">{{ product.prix }} €</p>
      <p class="description">{{ product.description }}</p>
    </div>
  </div>
</template>
```

## Composants d'authentification et profil

### Register.vue
Formulaire d'inscription avec validation des champs, gestion des erreurs (email déjà utilisé, champs obligatoires), et intégration API. Affiche un message d'erreur explicite en cas d'échec.

### Login.vue
Formulaire de connexion moderne, gestion des erreurs (identifiants invalides), redirection après connexion, affichage du message de bienvenue.

### ProfileView.vue
Permet à l'utilisateur connecté de modifier ses informations (nom, prénom, email, adresse, mot de passe). Validation côté client et gestion des erreurs serveur (email déjà utilisé, données invalides). Affiche un message de succès ou d'erreur.

### Navbar.vue
Affiche le prénom de l'utilisateur connecté, propose un menu utilisateur (profil, déconnexion), et adapte la navigation selon l'état d'authentification.

## Services API

### productService.js
Gère les appels API liés aux produits.

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export default {
  async getAllProducts() {
    const response = await axios.get(`${API_BASE_URL}/products`);
    return response.data;
  },

  async getProductById(id) {
    const response = await axios.get(`${API_BASE_URL}/products/${id}`);
    return response.data;
  }
};
```

### categoryService.js
Gère les appels API liés aux catégories.

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

export default {
  async getAllCategories() {
    const response = await axios.get(`${API_BASE_URL}/categories`);
    return response.data;
  }
};
```

### authService.js
Gère l'inscription, la connexion, la gestion du token JWT, le stockage local des informations utilisateur, la validation du token, la déconnexion, et la gestion automatique du rafraîchissement du token.

## Styles et Thème

### Configuration Tailwind
```javascript
// tailwind.config.js
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#f5f7fa',
          // ... autres nuances
        }
      }
    }
  }
};
```

### Classes CSS Personnalisées
```css
/* styles.css */
.product-card {
  @apply bg-white rounded-lg shadow-md overflow-hidden;
}

.product-image {
  @apply w-full h-48 object-cover;
}

.price {
  @apply text-xl font-bold text-primary-600;
}
```

## Fonctionnalités

### Navigation
- Routing avec Vue Router
- Navigation entre les catégories
- Affichage des détails produits

### Gestion des Images
- Intégration avec Unsplash
- Images de fallback
- Optimisation des images

### Responsive Design
- Breakpoints Bootstrap
- Classes Tailwind responsives
- Media queries personnalisées

## Bonnes Pratiques

### Performance
1. **Lazy Loading**
   - Composants chargés à la demande
   - Images optimisées

2. **Caching**
   - Mise en cache des données API
   - Stockage local des préférences

### Sécurité
1. **Validation**
   - Validation des entrées utilisateur
   - Protection XSS

2. **API**
   - Gestion des tokens
   - Headers sécurisés

### Maintenance
1. **Code**
   - Documentation des composants
   - Tests unitaires
   - Convention de nommage

2. **Dépendances**
   - Mise à jour régulière
   - Gestion des versions

## Gestion des erreurs
- Les messages d'erreur du backend sont affichés de façon explicite dans les formulaires (ex : "Cette adresse email est déjà utilisée").
- Les champs sont validés côté client avant envoi.
- Les erreurs globales sont affichées en haut du formulaire.

## Navigation et routes protégées
- Les routes /profile sont protégées et nécessitent une authentification.
- Les routes /login et /register redirigent vers l'accueil si l'utilisateur est déjà connecté.
- La Navbar s'adapte dynamiquement à l'état de connexion.

## Déploiement

### Build
```bash
npm run build
```

### Preview
```bash
npm run preview
```

### Production
1. Générer les fichiers statiques
2. Configurer le serveur web
3. Mettre en place le CDN
4. Configurer les variables d'environnement

## Développement

### Installation
```bash
npm install
```

### Développement local
```bash
npm run dev
```

### Tests
```bash
npm run test
```

### Linting
```bash
npm run lint
``` 