#!/bin/bash

# Script de test automatisé pour l'API de mise à jour des produits
# Usage: ./test-api.sh

set -e  # Arrêter le script en cas d'erreur

# Configuration
API_BASE_URL="http://localhost:8080/api"
GESTIONNAIRE_EMAIL="gestionnaire@test.com"
GESTIONNAIRE_PASSWORD="password"
CLIENT_EMAIL="client@test.com"
CLIENT_PASSWORD="password"

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonctions utilitaires
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Fonction pour obtenir un token JWT
get_token() {
    local email=$1
    local password=$2
    
    log_info "Connexion de l'utilisateur: $email"
    
    local response=$(curl -s -X POST "$API_BASE_URL/auth/login" \
        -H "Content-Type: application/json" \
        -d "{
            \"email\": \"$email\",
            \"password\": \"$password\"
        }")
    
    local token=$(echo $response | grep -o '"accessToken":"[^"]*"' | cut -d'"' -f4)
    
    if [ -z "$token" ]; then
        log_error "Échec de la connexion pour $email"
        echo $response
        return 1
    fi
    
    log_success "Token obtenu pour $email"
    echo $token
}

# Fonction pour tester la mise à jour d'un produit
test_update_product() {
    local token=$1
    local product_id=$2
    local expected_status=$3
    local test_name=$4
    
    log_info "Test: $test_name"
    
    local response=$(curl -s -w "\n%{http_code}" -X PUT "$API_BASE_URL/products/update/$product_id" \
        -H "Authorization: Bearer $token" \
        -H "Content-Type: application/json" \
        -d '{
            "nom": "Produit Test Modifié",
            "description": "Description de test mise à jour",
            "marque": "TestBrand",
            "prix": 49.99,
            "thumbnailUrl": "https://images.unsplash.com/photo-test",
            "genre": "FEMME",
            "taille": "S",
            "categoryId": 2
        }')
    
    local http_code=$(echo "$response" | tail -n1)
    local body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -eq "$expected_status" ]; then
        log_success "Test réussi: $test_name (HTTP $http_code)"
    else
        log_error "Test échoué: $test_name (HTTP $http_code attendu, $http_code reçu)"
        echo "Réponse: $body"
        return 1
    fi
}

# Fonction pour tester l'accès non autorisé
test_unauthorized_access() {
    local token=$1
    local test_name=$2
    
    log_info "Test: $test_name"
    
    local response=$(curl -s -w "\n%{http_code}" -X PUT "$API_BASE_URL/products/update/1" \
        -H "Authorization: Bearer $token" \
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
        }')
    
    local http_code=$(echo "$response" | tail -n1)
    local body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -eq 403 ]; then
        log_success "Test réussi: $test_name (HTTP 403)"
    else
        log_error "Test échoué: $test_name (HTTP 403 attendu, $http_code reçu)"
        echo "Réponse: $body"
        return 1
    fi
}

# Fonction pour tester l'accès sans authentification
test_no_auth() {
    log_info "Test: Accès sans authentification"
    
    local response=$(curl -s -w "\n%{http_code}" -X PUT "$API_BASE_URL/products/update/1" \
        -H "Content-Type: application/json" \
        -d '{
            "nom": "Test Sans Auth",
            "description": "Test",
            "marque": "Test",
            "prix": 10.00,
            "thumbnailUrl": "https://test.com",
            "genre": "HOMME",
            "taille": "M",
            "categoryId": 1
        }')
    
    local http_code=$(echo "$response" | tail -n1)
    
    if [ "$http_code" -eq 401 ]; then
        log_success "Test réussi: Accès sans authentification (HTTP 401)"
    else
        log_error "Test échoué: Accès sans authentification (HTTP 401 attendu, $http_code reçu)"
        return 1
    fi
}

# Fonction pour tester la validation des données
test_validation() {
    local token=$1
    
    log_info "Test: Validation des données"
    
    local response=$(curl -s -w "\n%{http_code}" -X PUT "$API_BASE_URL/products/update/1" \
        -H "Authorization: Bearer $token" \
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
        }')
    
    local http_code=$(echo "$response" | tail -n1)
    local body=$(echo "$response" | head -n -1)
    
    if [ "$http_code" -eq 400 ]; then
        log_success "Test réussi: Validation des données (HTTP 400)"
    else
        log_error "Test échoué: Validation des données (HTTP 400 attendu, $http_code reçu)"
        echo "Réponse: $body"
        return 1
    fi
}

# Vérification que le serveur est démarré
log_info "Vérification que le serveur backend est démarré..."
if ! curl -s "$API_BASE_URL/actuator/health" > /dev/null; then
    log_error "Le serveur backend n'est pas accessible sur $API_BASE_URL"
    log_info "Assurez-vous que le serveur Spring Boot est démarré sur le port 8080"
    exit 1
fi

log_success "Serveur backend accessible"

# Tests principaux
echo "=========================================="
echo "DÉBUT DES TESTS API - MISE À JOUR PRODUITS"
echo "=========================================="

# Test 1: Connexion du gestionnaire
log_info "Test 1: Connexion du gestionnaire"
GESTIONNAIRE_TOKEN=$(get_token "$GESTIONNAIRE_EMAIL" "$GESTIONNAIRE_PASSWORD")

# Test 2: Mise à jour réussie d'un produit (CA1 et CA4)
test_update_product "$GESTIONNAIRE_TOKEN" 1 200 "Mise à jour réussie d'un produit"

# Test 3: Produit inexistant (CA2)
test_update_product "$GESTIONNAIRE_TOKEN" 999 404 "Produit inexistant"

# Test 4: Connexion du client
log_info "Test 4: Connexion du client"
CLIENT_TOKEN=$(get_token "$CLIENT_EMAIL" "$CLIENT_PASSWORD")

# Test 5: Accès non autorisé (CA3)
test_unauthorized_access "$CLIENT_TOKEN" "Accès non autorisé pour un client"

# Test 6: Accès sans authentification
test_no_auth

# Test 7: Validation des données
test_validation "$GESTIONNAIRE_TOKEN"

echo "=========================================="
echo "FIN DES TESTS API"
echo "=========================================="

log_success "Tous les tests API sont passés avec succès !"
log_info "Vous pouvez maintenant tester l'interface frontend"

# Instructions pour les tests frontend
echo ""
echo "=========================================="
echo "PROCHAINES ÉTAPES - TESTS FRONTEND"
echo "=========================================="
echo "1. Démarrer le frontend: cd frontend && npm run dev"
echo "2. Ouvrir http://localhost:5173"
echo "3. Se connecter avec gestionnaire@test.com / password"
echo "4. Tester l'interface d'administration"
echo "5. Consulter le guide TEST_GUIDE.md pour plus de détails" 