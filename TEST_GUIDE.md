# Guide de Test - User Story "Mise à jour des produits par le gestionnaire"

## 🎯 **Objectif**
Valider que tous les critères d'acceptation sont respectés et que l'implémentation fonctionne correctement.

## 📋 **Critères d'Acceptation à Tester**

### **CA1 : Le gestionnaire peut modifier n'importe quel champ du produit**
### **CA2 : Si le produit n'existe pas, une erreur claire est affichée**
### **CA3 : Les rôles non autorisés reçoivent une erreur 403 (interdit)**
### **CA4 : Un message confirme la mise à jour réussie**

---

## 🗄️ **Préparation de la Base de Données**

### **1. Exécuter le script de préparation**
```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script
source test-database-setup.sql
```

### **2. Vérifier les données de test**
- Utilisateur gestionnaire : `gestionnaire@test.com` / `password`
- Utilisateur client : `client@test.com` / `password`
- Produits de test avec IDs 1, 2, 3

---

## 🚀 **Démarrage des Applications**

### **Backend (Port 8080)**
```bash
# Dans le dossier racine du projet
mvn spring-boot:run
```

### **Frontend (Port 5173)**
```bash
# Dans le dossier frontend
cd frontend
npm run dev
```

---

## 🧪 **Tests Backend (API)**

### **Test 1 : Connexion du gestionnaire**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "gestionnaire@test.com",
    "password": "password"
  }'
```
**Résultat attendu :** Token JWT retourné

### **Test 2 : Mise à jour d'un produit (succès)**
```bash
# Remplacer {TOKEN} par le token obtenu
curl -X PUT http://localhost:8080/api/products/update/1 \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "T-shirt Basic Homme - Modifié",
    "description": "Description mise à jour du t-shirt",
    "marque": "EcoFashion",
    "prix": 34.99,
    "thumbnailUrl": "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400",
    "genre": "HOMME",
    "taille": "L",
    "categoryId": 1
  }'
```
**Résultat attendu :** 200 OK avec message de succès

### **Test 3 : Produit inexistant (CA2)**
```bash
curl -X PUT http://localhost:8080/api/products/update/999 \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Produit Inexistant",
    "description": "Test",
    "marque": "Test",
    "prix": 10.00,
    "thumbnailUrl": "https://test.com",
    "genre": "HOMME",
    "taille": "M",
    "categoryId": 1
  }'
```
**Résultat attendu :** 404 Not Found avec message "Produit non trouvé"

### **Test 4 : Accès non autorisé (CA3)**
```bash
# Connexion avec un client
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "client@test.com",
    "password": "password"
  }'

# Tentative de mise à jour avec le token client
curl -X PUT http://localhost:8080/api/products/update/1 \
  -H "Authorization: Bearer {TOKEN_CLIENT}" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test Non Autorisé",
    "description": "Test",
    "marque": "Test",
    "prix": 10.00,
    "thumbnailUrl": "https://test.com",
    "genre": "HOMME",
    "taille": "M",
    "categoryId": 1
  }'
```
**Résultat attendu :** 403 Forbidden avec message "Accès refusé"

### **Test 5 : Validation des données**
```bash
curl -X PUT http://localhost:8080/api/products/update/1 \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "",
    "description": "Test",
    "marque": "Test",
    "prix": -10.00,
    "thumbnailUrl": "invalid-url",
    "genre": "INVALID",
    "taille": "INVALID",
    "categoryId": 999
  }'
```
**Résultat attendu :** 400 Bad Request avec erreurs de validation

---

## 🎨 **Tests Frontend (Interface)**

### **Test 1 : Accès à la page d'administration**

1. **Se connecter en tant que gestionnaire**
   - Aller sur `http://localhost:5173/login`
   - Email : `gestionnaire@test.com`
   - Mot de passe : `password`

2. **Accéder à l'administration**
   - Cliquer sur le menu utilisateur (avatar)
   - Vérifier que le lien "Administration" est visible
   - Cliquer sur "Administration"

3. **Vérifier la page d'administration**
   - URL : `http://localhost:5173/admin/products`
   - Liste des produits affichée
   - Bouton "Modifier" pour chaque produit

### **Test 2 : Modification d'un produit (CA1)**

1. **Cliquer sur "Modifier" pour un produit**
2. **Modifier tous les champs :**
   - Nom : "Produit Test Modifié"
   - Description : "Description de test mise à jour"
   - Marque : "TestBrand"
   - Prix : 49.99
   - URL : "https://images.unsplash.com/photo-test"
   - Genre : "FEMME"
   - Taille : "S"
   - Catégorie : "Vêtements Femme"

3. **Cliquer sur "Enregistrer"**
4. **Vérifier le message de succès (CA4)**

### **Test 3 : Validation côté client**

1. **Tester les validations :**
   - Nom vide → Erreur affichée
   - Prix négatif → Erreur affichée
   - URL invalide → Erreur affichée
   - Description trop courte → Erreur affichée

2. **Vérifier l'aperçu d'image**
   - URL valide → Aperçu affiché
   - URL invalide → Image par défaut

### **Test 4 : Accès non autorisé (CA3)**

1. **Se connecter en tant que client**
   - Email : `client@test.com`
   - Mot de passe : `password`

2. **Tenter d'accéder à l'administration**
   - Aller directement sur `http://localhost:5173/admin/products`
   - **Résultat attendu :** Redirection vers le profil

3. **Vérifier l'absence du lien d'administration**
   - Menu utilisateur → Pas de lien "Administration"

### **Test 5 : Gestion des erreurs**

1. **Tester avec un produit inexistant**
   - Modifier l'URL pour un ID inexistant
   - **Résultat attendu :** Message d'erreur 404

2. **Tester la déconnexion**
   - Se déconnecter
   - Tenter d'accéder à l'administration
   - **Résultat attendu :** Redirection vers login

---

## 🔍 **Tests de Sécurité**

### **Test 1 : Protection des endpoints**
```bash
# Test sans token
curl -X PUT http://localhost:8080/api/products/update/1 \
  -H "Content-Type: application/json" \
  -d '{"nom": "Test"}'
```
**Résultat attendu :** 401 Unauthorized

### **Test 2 : Token expiré**
```bash
# Utiliser un token expiré
curl -X PUT http://localhost:8080/api/products/update/1 \
  -H "Authorization: Bearer expired_token" \
  -H "Content-Type: application/json" \
  -d '{"nom": "Test"}'
```
**Résultat attendu :** 401 Unauthorized

### **Test 3 : Injection SQL**
```bash
curl -X PUT http://localhost:8080/api/products/update/1 \
  -H "Authorization: Bearer {TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Test\"; DROP TABLE product; --",
    "description": "Test",
    "marque": "Test",
    "prix": 10.00,
    "thumbnailUrl": "https://test.com",
    "genre": "HOMME",
    "taille": "M",
    "categoryId": 1
  }'
```
**Résultat attendu :** Validation échoue, pas d'injection

---

## 📊 **Validation des Critères d'Acceptation**

### ✅ **CA1 : Modification de tous les champs**
- [ ] Tous les champs sont modifiables dans le formulaire
- [ ] Les modifications sont sauvegardées en base
- [ ] L'audit trail fonctionne (updatedAt, updatedBy)

### ✅ **CA2 : Erreur produit inexistant**
- [ ] Erreur 404 retournée pour ID inexistant
- [ ] Message clair affiché à l'utilisateur
- [ ] Gestion côté frontend et backend

### ✅ **CA3 : Protection par rôle**
- [ ] Erreur 403 pour les rôles non autorisés
- [ ] Redirection automatique côté frontend
- [ ] Lien d'administration caché pour les non-gestionnaires

### ✅ **CA4 : Message de succès**
- [ ] Message affiché après mise à jour réussie
- [ ] Message disparaît automatiquement
- [ ] Feedback visuel approprié

---

## 🐛 **Dépannage**

### **Problèmes courants :**

1. **Erreur de connexion à la base de données**
   - Vérifier que MySQL est démarré
   - Vérifier les paramètres dans `application.properties`

2. **Erreur CORS**
   - Vérifier que le backend tourne sur le port 8080
   - Vérifier la configuration CORS dans SecurityConfig

3. **Token JWT invalide**
   - Vérifier la clé secrète dans `application.properties`
   - Vérifier l'expiration du token

4. **Erreur 404 sur le frontend**
   - Vérifier que le serveur de développement tourne
   - Vérifier les routes dans le router Vue.js

---

## ✅ **Checklist Finale**

- [ ] Base de données préparée avec les données de test
- [ ] Backend démarré sur le port 8080
- [ ] Frontend démarré sur le port 5173
- [ ] Tests API réussis
- [ ] Tests frontend réussis
- [ ] Tests de sécurité réussis
- [ ] Tous les critères d'acceptation validés

**🎉 Félicitations ! Votre user story est implémentée et testée avec succès !** 