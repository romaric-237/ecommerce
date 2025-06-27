# Test : Restriction du panier pour les gestionnaires

## 🎯 Objectif
Vérifier que les gestionnaires ne peuvent pas ajouter de produits au panier, conformément à la logique métier.

## 📋 Tests à effectuer

### Test 1 : Connexion en tant que gestionnaire
1. Se connecter avec `gestionnaire@test.com` / `password`
2. Vérifier que le rôle "GESTIONNAIRE" s'affiche
3. **Résultat attendu** : ✅ Connexion réussie

### Test 2 : Vérification de l'interface
1. Aller sur la page d'accueil ou la liste des produits
2. Observer les boutons "Ajouter au panier"
3. **Résultat attendu** : ❌ Les boutons "Ajouter au panier" sont masqués
4. **Résultat attendu** : ✅ Un message "Gestionnaire" s'affiche à la place

### Test 3 : Vérification du panier dans la navbar
1. Regarder la navbar
2. **Résultat attendu** : ❌ L'icône du panier n'est pas visible

### Test 4 : Test de la page détail produit
1. Cliquer sur "Voir détails" d'un produit
2. Observer la section des actions
3. **Résultat attendu** : ❌ Pas de bouton "Ajouter au panier"
4. **Résultat attendu** : ✅ Message "En tant que gestionnaire, vous ne pouvez pas ajouter de produits au panier"

### Test 5 : Test API (Backend)
1. Essayer d'ajouter un produit au panier via l'API
2. **Résultat attendu** : ❌ Erreur 403 (Forbidden)

### Test 6 : Connexion en tant que client
1. Se déconnecter
2. Se connecter avec `client@test.com` / `password`
3. Vérifier que les boutons "Ajouter au panier" sont visibles
4. **Résultat attendu** : ✅ Panier fonctionnel pour les clients

## 🔧 Commandes de test

### Test API avec curl
```bash
# Connexion gestionnaire
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "gestionnaire@test.com", "password": "password"}'

# Essayer d'ajouter au panier (doit échouer)
curl -X POST "http://localhost:8080/api/cart/add?userId=1&productId=1&quantite=1" \
  -H "Authorization: Bearer {TOKEN_GESTIONNAIRE}"
```

## ✅ Critères de succès
- [ ] Les gestionnaires ne voient pas les boutons "Ajouter au panier"
- [ ] L'icône du panier est masquée dans la navbar pour les gestionnaires
- [ ] L'API retourne une erreur 403 pour les gestionnaires
- [ ] Les clients peuvent toujours utiliser le panier normalement
- [ ] Messages informatifs appropriés pour les gestionnaires 