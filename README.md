
# E-Commerce Application

## Description
Application e-commerce développée avec Spring Boot (Backend) et Vue.js (Frontend), utilisant une architecture moderne et des technologies à jour.

## Technologies Utilisées

### Backend
- Java 17
- Spring Boot 3.2.3
- Spring Data JPA
- MySQL
- Maven

### Frontend
- Vue.js 3
- Vite
- Bootstrap 5
- Tailwind CSS
- Axios

## Prérequis
- JDK 17 ou supérieur
- Node.js 18 ou supérieur
- MySQL 8.0 ou supérieur
- Maven 3.8 ou supérieur

## Installation

### Backend
1. Cloner le repository
```bash
git clone [URL_DU_REPO]
```

2. Configurer la base de données
- Créer une base de données MySQL nommée `ecommerce`
- Exécuter le script SQL `src/main/resources/product.sql`

3. Configurer l'application
- Modifier `application.properties` selon votre environnement
- Clés API requises :
  - Unsplash API Key (pour les images de produits)

4. Lancer l'application
```bash
cd backend
mvn spring-boot:run
```

### Frontend
1. Installer les dépendances
```bash
cd frontend
npm install
```

2. Configurer les variables d'environnement
- Créer un fichier `.env` basé sur `.env.example`
- Configurer les URLs de l'API

3. Lancer l'application
```bash
npm run dev
```

## Structure du Projet

### Backend
```
src/main/java/com/example/ecommerce/
├── controller/    # Contrôleurs REST
├── model/        # Entités JPA
├── repository/   # Repositories Spring Data
├── service/      # Services métier
└── config/       # Configurations
```

### Frontend
```
frontend/
├── src/
│   ├── components/    # Composants Vue.js
│   ├── views/         # Vues principales
│   ├── services/      # Services API
│   ├── router/        # Configuration des routes
│   └── assets/        # Ressources statiques
└── public/           # Fichiers publics
```

## Fonctionnalités

### Gestion des Produits
- Affichage de la liste des produits
- Détails des produits
- Recherche par catégorie
- Images dynamiques via Unsplash

### Gestion des Catégories
- Liste des catégories
- Produits par catégorie
- Navigation intuitive

### Interface Utilisateur
- Design responsive
- Thème moderne et professionnel
- Intégration Bootstrap et Tailwind CSS
- Animations et transitions fluides

## API Endpoints

### Produits
- `GET /api/products` - Liste tous les produits
- `GET /api/products/{id}` - Détails d'un produit
- `GET /api/products/category/{id}` - Produits par catégorie

### Catégories
- `GET /api/categories` - Liste toutes les catégories
- `GET /api/categories/{id}` - Détails d'une catégorie

## Configuration

### Backend
```properties
# application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Frontend
```env
# .env
VITE_API_BASE_URL=http://localhost:8080/api
VITE_UNSPLASH_API_KEY=your_unsplash_api_key
```

## Déploiement

### Backend
1. Générer le JAR
```bash
mvn clean package
```

2. Exécuter le JAR
```bash
java -jar target/ecommerce-0.0.1-SNAPSHOT.jar
```

### Frontend
1. Build de production
```bash
npm run build
```

2. Servir les fichiers statiques
```bash
npm run preview
```

## Contribution
1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## Licence
Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## Contact
[Votre Nom] - [Votre Email]

## Remerciements
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Vue.js](https://vuejs.org/)
- [Bootstrap](https://getbootstrap.com/)
- [Tailwind CSS](https://tailwindcss.com/)
- [Unsplash API](https://unsplash.com/developers) 