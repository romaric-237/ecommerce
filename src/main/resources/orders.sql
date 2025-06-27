-- Script de création des tables pour la gestion des commandes
-- À exécuter sur votre base de données MySQL

USE ecommerce;

-- 1. Création de la table orders
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id INT NOT NULL,
    order_date DATETIME NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(20),
    total_amount DECIMAL(10,2) NOT NULL,
    
    -- Adresse de facturation
    billing_address VARCHAR(255),
    billing_postal_code VARCHAR(10),
    billing_city VARCHAR(100),
    
    -- Adresse de livraison
    shipping_address VARCHAR(255) NOT NULL,
    shipping_postal_code VARCHAR(10) NOT NULL,
    shipping_city VARCHAR(100) NOT NULL,
    shipping_country VARCHAR(100) NOT NULL DEFAULT 'France',
    
    -- Informations de contact pour la livraison
    shipping_phone VARCHAR(20),
    shipping_email VARCHAR(255),
    
    -- Commentaires
    comments TEXT,
    
    -- Champs d'audit
    created_at DATETIME NOT NULL,
    updated_at DATETIME NOT NULL,
    
    -- Contraintes
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_order_number (order_number),
    INDEX idx_status (status),
    INDEX idx_order_date (order_date)
);

-- 2. Création de la table order_item
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    
    -- Contraintes
    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES product(id) ON DELETE RESTRICT,
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id)
);

-- 3. Insertion de données de test pour les commandes
INSERT INTO orders (
    order_number, 
    user_id, 
    order_date, 
    status, 
    payment_method, 
    total_amount,
    billing_address,
    billing_postal_code,
    billing_city,
    shipping_address,
    shipping_postal_code,
    shipping_city,
    shipping_country,
    shipping_phone,
    shipping_email,
    created_at,
    updated_at
) VALUES 
(
    'CMD-1703123456789-123',
    2, -- client@test.com
    NOW(),
    'PAID',
    'CREDIT_CARD',
    89.98,
    '456 Avenue Client',
    '75002',
    'Paris',
    '789 Rue de Livraison',
    '75003',
    'Paris',
    'France',
    '0123456789',
    'client@test.com',
    NOW(),
    NOW()
),
(
    'CMD-1703123456790-456',
    2, -- client@test.com
    NOW(),
    'PENDING',
    'PAYPAL',
    45.00,
    '456 Avenue Client',
    '75002',
    'Paris',
    '456 Avenue Client',
    '75002',
    'Paris',
    'France',
    '0123456789',
    'client@test.com',
    NOW(),
    NOW()
);

-- 4. Insertion de données de test pour les articles de commande
INSERT INTO order_item (order_id, product_id, quantity, price) VALUES
(1, 1, 2, 29.99), -- 2x T-shirt Basic Homme
(1, 2, 1, 59.99), -- 1x Jean Slim Femme
(2, 3, 1, 45.00); -- 1x Sac à dos urbain

-- 5. Vérification des données insérées
SELECT '=== COMMANDES DE TEST ===' as info;
SELECT 
    o.id,
    o.order_number,
    u.email as client,
    o.status,
    o.payment_method,
    o.total_amount,
    o.shipping_city,
    o.created_at
FROM orders o
JOIN user u ON o.user_id = u.id
ORDER BY o.id;

SELECT '=== ARTICLES DE COMMANDE ===' as info;
SELECT 
    oi.id,
    o.order_number,
    p.nom as produit,
    oi.quantity,
    oi.price,
    (oi.quantity * oi.price) as total_ligne
FROM order_item oi
JOIN orders o ON oi.order_id = o.id
JOIN product p ON oi.product_id = p.id
ORDER BY oi.id;

-- 6. Statistiques
SELECT '=== STATISTIQUES ===' as info;
SELECT 
    COUNT(*) as total_commandes,
    SUM(total_amount) as chiffre_affaires,
    AVG(total_amount) as panier_moyen
FROM orders;

SELECT 
    status,
    COUNT(*) as nombre
FROM orders
GROUP BY status; 