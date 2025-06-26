-- Script pour ajouter les colonnes d'audit à la table product
-- À exécuter sur votre base de données MySQL

USE ecommerce;

-- Ajouter les colonnes d'audit si elles n'existent pas déjà
ALTER TABLE product 
ADD COLUMN IF NOT EXISTS updated_at DATETIME NULL COMMENT 'Date et heure de la dernière modification',
ADD COLUMN IF NOT EXISTS updated_by VARCHAR(255) NULL COMMENT 'Utilisateur qui a effectué la dernière modification';

-- Ajouter un index sur updated_at pour optimiser les requêtes d'audit
CREATE INDEX IF NOT EXISTS idx_product_updated_at ON product(updated_at);

-- Mettre à jour les enregistrements existants avec une valeur par défaut
UPDATE product 
SET updated_at = NOW(), 
    updated_by = 'system' 
WHERE updated_at IS NULL;

-- Afficher la structure de la table pour vérification
DESCRIBE product; 