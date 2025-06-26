# Tests Manuels - Mise à jour des produits par les gestionnaires

## Prérequis
- Serveur backend Spring Boot démarré sur le port 8080
- Serveur frontend Vue.js démarré sur le port 5173
- Base de données avec des produits de test

## 1. Test de connexion gestionnaire

### Étapes :
1. Aller sur `http://localhost:5173` (frontend)
2. Cliquer sur "Se connecter"
3. Utiliser les identifiants d'un gestionnaire :
   - Email : `gestionnaire@test.com`
   - Mot de passe : `password123`
4. Vérifier que la connexion réussit
5. Vérifier que le rôle "GESTIONNAIRE" s'affiche

### Résultat attendu :
- ✅ Connexion réussie
- ✅ Redirection vers la page d'accueil
- ✅ Menu "Gestion des produits" visible

## 2. Test d'accès à la page de gestion

### Étapes :
1. Cliquer sur "Gestion des produits" dans le menu
2. Vérifier l'URL : `http://localhost:5173/admin/products`

### Résultat attendu :
- ✅ Page de gestion des produits s'affiche
- ✅ Liste des produits visible
- ✅ Boutons "Modifier" pour chaque produit

## 3. Test de modification d'un produit

### Étapes :
1. Cliquer sur "Modifier" pour un produit
2. Vérifier que le formulaire s'ouvre avec les données actuelles
3. Modifier les champs suivants :
   - Nom : "Test Modification"
   - Prix : 99.99
   - Description : "Produit modifié pour test"
4. Cliquer sur "Enregistrer"

### Résultat attendu :
- ✅ Formulaire pré-rempli avec les données actuelles
- ✅ Validation des champs
- ✅ Message de succès après sauvegarde
- ✅ Produit mis à jour dans la liste

## 4. Test de validation des champs

### Étapes :
1. Ouvrir le formulaire de modification
2. Vider le champ "Nom"
3. Mettre un prix négatif
4. Cliquer sur "Enregistrer"

### Résultat attendu :
- ✅ Messages d'erreur s'affichent
- ✅ Formulaire ne se soumet pas
- ✅ Champs invalides sont mis en évidence

## 5. Test d'annulation

### Étapes :
1. Ouvrir le formulaire de modification
2. Modifier quelques champs
3. Cliquer sur "Annuler"

### Résultat attendu :
- ✅ Formulaire se ferme
- ✅ Aucune modification n'est sauvegardée
- ✅ Retour à la liste des produits

## 6. Test de permissions

### Étapes :
1. Se déconnecter
2. Se connecter avec un compte utilisateur normal
3. Essayer d'accéder à `http://localhost:5173/admin/products`

### Résultat attendu :
- ✅ Accès refusé
- ✅ Redirection vers la page de profil ou d'accueil
- ✅ Message d'erreur approprié

## 7. Test de recherche et filtrage

### Étapes :
1. Dans la page de gestion, utiliser la barre de recherche
2. Taper le nom d'un produit
3. Vérifier que la liste se filtre

### Résultat attendu :
- ✅ Recherche en temps réel
- ✅ Résultats filtrés correctement
- ✅ Possibilité de réinitialiser les filtres

## 8. Test de pagination

### Étapes :
1. Créer plus de 10 produits de test
2. Vérifier que la pagination s'affiche
3. Naviguer entre les pages

### Résultat attendu :
- ✅ Pagination fonctionnelle
- ✅ Navigation entre les pages
- ✅ Nombre correct de produits par page

## 9. Test de responsive

### Étapes :
1. Tester sur mobile (F12 → Device toolbar)
2. Vérifier l'affichage du formulaire
3. Tester les interactions tactiles

### Résultat attendu :
- ✅ Interface adaptée au mobile
- ✅ Formulaire utilisable sur petit écran
- ✅ Boutons et champs accessibles

## 10. Test de performance

### Étapes :
1. Charger la page avec beaucoup de produits
2. Mesurer le temps de chargement
3. Tester la recherche avec beaucoup de données

### Résultat attendu :
- ✅ Temps de chargement acceptable (< 3 secondes)
- ✅ Recherche fluide
- ✅ Pas de blocage de l'interface

## Configuration des ports

### Frontend (Vue.js) :
- **URL** : `http://localhost:5173`
- **Port** : 5173
- **Commandes** :
  ```bash
  cd frontend
  npm run dev
  ```

### Backend (Spring Boot) :
- **URL** : `http://localhost:8080`
- **Port** : 8080
- **Commandes** :
  ```bash
  cd backend
  ./mvnw spring-boot:run
  ```

## Notes importantes

- **Sauvegarder** les données de test avant de commencer
- **Documenter** les bugs trouvés
- **Tester** sur différents navigateurs
- **Vérifier** les logs du serveur pendant les tests
- **Vérifier** que les deux serveurs sont démarrés avant de tester 