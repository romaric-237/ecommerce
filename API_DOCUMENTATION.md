# Documentation de l'API E-Commerce

## Base URL
```
http://localhost:8080/api
```

## Authentification
L'API utilise une authentification basique. Les en-têtes requis sont :
```
Authorization: Basic {credentials}
Content-Type: application/json
```

## Endpoints

### Produits

#### Liste tous les produits
```http
GET /products
```

Réponse :
```json
[
  {
    "id": 1,
    "nom": "Nom du produit",
    "description": "Description du produit",
    "prix": 99.99,
    "marque": "Marque",
    "genre": "Homme",
    "taille": "M",
    "stock": 10,
    "categoryId": 1,
    "categoryNom": "Nom de la catégorie"
  }
]
```

#### Obtenir un produit par ID
```http
GET /products/{id}
```

Réponse :
```json
{
  "id": 1,
  "nom": "Nom du produit",
  "description": "Description du produit",
  "prix": 99.99,
  "marque": "Marque",
  "genre": "Homme",
  "taille": "M",
  "stock": 10,
  "categoryId": 1,
  "categoryNom": "Nom de la catégorie"
}
```

#### Obtenir les produits par catégorie
```http
GET /products/category/{categoryId}
```

Réponse :
```json
[
  {
    "id": 1,
    "nom": "Nom du produit",
    "description": "Description du produit",
    "prix": 99.99,
    "marque": "Marque",
    "genre": "Homme",
    "taille": "M",
    "stock": 10,
    "categoryId": 1,
    "categoryNom": "Nom de la catégorie"
  }
]
```

### Catégories

#### Liste toutes les catégories
```http
GET /categories
```

Réponse :
```json
[
  {
    "id": 1,
    "nom": "Nom de la catégorie",
    "description": "Description de la catégorie"
  }
]
```

#### Obtenir une catégorie par ID
```http
GET /categories/{id}
```

Réponse :
```json
{
  "id": 1,
  "nom": "Nom de la catégorie",
  "description": "Description de la catégorie"
}
```

## Codes d'erreur

| Code | Description |
|------|-------------|
| 200 | Succès |
| 201 | Créé avec succès |
| 400 | Requête invalide |
| 401 | Non autorisé |
| 403 | Accès interdit |
| 404 | Ressource non trouvée |
| 500 | Erreur serveur |

## Exemples d'utilisation

### Exemple avec cURL

#### Obtenir tous les produits
```bash
curl -X GET http://localhost:8080/api/products
```

#### Obtenir un produit spécifique
```bash
curl -X GET http://localhost:8080/api/products/1
```

#### Obtenir les produits d'une catégorie
```bash
curl -X GET http://localhost:8080/api/products/category/1
```

### Exemple avec Axios (JavaScript)

```javascript
import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

// Obtenir tous les produits
const getProducts = async () => {
  try {
    const response = await axios.get(`${API_BASE_URL}/products`);
    return response.data;
  } catch (error) {
    console.error('Erreur lors de la récupération des produits:', error);
    throw error;
  }
};

// Obtenir un produit par ID
const getProductById = async (id) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/products/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Erreur lors de la récupération du produit ${id}:`, error);
    throw error;
  }
};
```

## Bonnes pratiques

1. **Gestion des erreurs**
   - Toujours vérifier les codes d'erreur
   - Implémenter une gestion appropriée des erreurs côté client

2. **Performance**
   - Utiliser la pagination pour les grandes listes
   - Mettre en cache les réponses fréquemment utilisées

3. **Sécurité**
   - Toujours utiliser HTTPS en production
   - Valider les entrées côté serveur
   - Implémenter une limitation de taux (rate limiting)

4. **Maintenance**
   - Documenter les changements d'API
   - Maintenir la rétrocompatibilité
   - Utiliser le versioning de l'API 