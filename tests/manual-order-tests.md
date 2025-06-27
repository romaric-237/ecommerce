# Tests Manuels - Gestion de Commande et Paiement

## 1. Parcours d'achat complet
1. Se connecter avec un compte client
2. Ajouter plusieurs produits au panier
3. Aller sur la page de paiement (checkout)
4. Remplir l'adresse de livraison (différente de l'adresse de facturation si souhaité)
5. Choisir une méthode de paiement (carte, PayPal, etc.)
6. Valider la commande
7. Vérifier le message de succès et l'email de confirmation (si implémenté)
8. Vérifier que le panier est vidé

## 2. Paiement échoué
1. Simuler un échec de paiement (ex : montant à 0, méthode non sélectionnée, etc.)
2. Vérifier le message d'erreur et la redirection vers l'accueil

## 3. Stock insuffisant
1. Ajouter au panier plus d'articles que le stock disponible
2. Tenter de valider la commande
3. Vérifier que la commande est refusée et qu'un message d'erreur s'affiche

## 4. Historique des commandes
1. Aller sur la page "Mes commandes"
2. Vérifier que toutes les commandes passées s'affichent
3. Cliquer sur le détail d'une commande pour voir les articles, adresses, statut, etc.

## 5. Cas limites
- Panier vide : vérifier qu'on ne peut pas accéder au checkout
- Mauvais format d'adresse ou email : vérifier la validation des champs
- Déconnexion pendant le checkout : vérifier la gestion de session

## 6. Responsive et UX
- Tester le parcours sur mobile et tablette
- Vérifier l'affichage des formulaires et tableaux
- Tester la navigation entre les étapes du checkout

## 7. Sécurité
- Tenter d'accéder à l'API des commandes sans être connecté : doit être refusé
- Tenter de valider une commande avec un autre utilisateur : doit être refusé 