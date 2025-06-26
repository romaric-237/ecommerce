-- Script de préparation de la base de données pour les tests
-- À exécuter sur votre base de données MySQL

USE ecommerce;

-- 1. Ajouter les colonnes d'audit si elles n'existent pas
ALTER TABLE product 
ADD COLUMN IF NOT EXISTS updated_at DATETIME NULL COMMENT 'Date et heure de la dernière modification',
ADD COLUMN IF NOT EXISTS updated_by VARCHAR(255) NULL COMMENT 'Utilisateur qui a effectué la dernière modification';

-- 2. Créer un utilisateur gestionnaire de test
INSERT INTO user (nom, prenom, email, password, adresse, code_postal, ville, enabled, role) 
VALUES (
    'Admin',
    'Test',
    'gestionnaire@test.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password: password
    '123 Rue de Test',
    '75001',
    'Paris',
    true,
    'GESTIONNAIRE'
) ON DUPLICATE KEY UPDATE role = 'GESTIONNAIRE';

-- 3. Créer un utilisateur client de test
INSERT INTO user (nom, prenom, email, password, adresse, code_postal, ville, enabled, role) 
VALUES (
    'Client',
    'Test',
    'client@test.com',
    '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', -- password: password
    '456 Avenue Client',
    '75002',
    'Paris',
    true,
    'CLIENT'
) ON DUPLICATE KEY UPDATE role = 'CLIENT';

-- 4. Insérer des catégories de test si elles n'existent pas
INSERT INTO category (nom, description) VALUES
('Vêtements Homme', 'Vêtements pour hommes'),
('Vêtements Femme', 'Vêtements pour femmes'),
('Accessoires', 'Accessoires de mode')
ON DUPLICATE KEY UPDATE nom = VALUES(nom);

-- 5. Insérer des produits de test
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id, updated_at, updated_by) VALUES
('T-shirt Basic Homme', 'T-shirt en coton bio pour homme', 'EcoFashion', 29.99, 'https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400', 'HOMME', 'M', 1, NOW(), 'system'),
('Jean Slim Femme', 'Jean slim stretch pour femme', 'DenimCo', 59.99, 'https://images.unsplash.com/photo-1542272604-787c3835535d?w=400', 'FEMME', 'S', 2, NOW(), 'system'),
('Sac à dos urbain', 'Sac à dos moderne et pratique', 'UrbanStyle', 45.00, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400', 'UNISEXE', 'M', 3, NOW(), 'system')
ON DUPLICATE KEY UPDATE 
    nom = VALUES(nom),
    description = VALUES(description),
    marque = VALUES(marque),
    prix = VALUES(prix),
    thumbnail_url = VALUES(thumbnail_url),
    genre = VALUES(genre),
    taille = VALUES(taille),
    category_id = VALUES(category_id);

-- 6. Afficher les données de test
SELECT '=== UTILISATEURS DE TEST ===' as info;
SELECT id, nom, prenom, email, role FROM user WHERE email IN ('gestionnaire@test.com', 'client@test.com');

SELECT '=== CATÉGORIES ===' as info;
SELECT id, nom, description FROM category;

SELECT '=== PRODUITS DE TEST ===' as info;
SELECT p.id, p.nom, p.marque, p.prix, c.nom as categorie, p.updated_at, p.updated_by 
FROM product p 
LEFT JOIN category c ON p.category_id = c.id 
ORDER BY p.id;

-- 7. Vérifier la structure de la table product
DESCRIBE product; 