-- Ajout du champ stock à la table product si besoin
ALTER TABLE product ADD COLUMN IF NOT EXISTS stock INT NOT NULL DEFAULT 0;

-- Mise à jour de tous les produits existants pour leur donner un stock de base
UPDATE product SET stock = 10 WHERE stock IS NULL;

-- Vérification
SELECT id, nom, stock FROM product; 