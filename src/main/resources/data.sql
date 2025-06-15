INSERT INTO categorie (nom, description) VALUES
                                             ('T-shirts', 'T-shirts pour homme et femme'),
                                             ('Jeans', 'Pantalons jeans toutes tailles'),
                                             ('Vestes', 'Vestes légères et manteaux'),
                                             ('Baskets', 'Chaussures de sport'),
                                             ('Bottes', 'Bottes en cuir et bottines'),
                                             ('Sandales', 'Sandales d’été'),
                                             ('Chemises', 'Chemises habillées ou casual'),
                                             ('Pulls', 'Pulls chauds pour l’hiver'),
                                             ('Robes', 'Robes toutes saisons'),
                                             ('Accessoires', 'Ceintures, sacs, chapeaux');




-- T-shirts (category_id = 1)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('T-shirt blanc basique', 'Coton bio 100%', 'H&M', 14.99, 'thumbs/tshirt1.jpg', 'HOMME', 'M', 1),
                                                                                                    ('T-shirt graphique', 'Design imprimé moderne', 'Zara', 19.99, 'thumbs/tshirt2.jpg', 'FEMME', 'S', 1),
                                                                                                    ('T-shirt oversize', 'Style décontracté', 'Nike', 29.90, 'thumbs/tshirt3.jpg', 'HOMME', 'L', 1),
                                                                                                    ('T-shirt manches longues', 'Idéal pour la mi-saison', 'Uniqlo', 24.50, 'thumbs/tshirt4.jpg', 'FEMME', 'M', 1);

-- Jeans (category_id = 2)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Jean slim noir', 'Stretch et confortable', 'Levi\'s', 49.99, 'thumbs/jean1.jpg', 'HOMME', 'L', 2),
                                                                                                    ('Jean flare bleu', 'Retour des années 90', 'Zara', 39.99, 'thumbs/jean2.jpg', 'FEMME', 'M', 2),
                                                                                                    ('Jean droit brut', 'Coupe classique', 'Celio', 35.00, 'thumbs/jean3.jpg', 'HOMME', 'XL', 2),
                                                                                                    ('Jean skinny', 'Ajusté et moderne', 'H&M', 32.90, 'thumbs/jean4.jpg', 'FEMME', 'S', 2);

-- Vestes (category_id = 3)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Veste en jean', 'Indémodable', 'Levi\'s', 79.90, 'thumbs/veste1.jpg', 'HOMME', 'L', 3),
                                                                                                    ('Blazer noir', 'Élégance assurée', 'Zara', 59.99, 'thumbs/veste2.jpg', 'FEMME', 'M', 3),
                                                                                                    ('Veste matelassée', 'Mi-saison', 'Uniqlo', 89.90, 'thumbs/veste3.jpg', 'HOMME', 'XL', 3),
                                                                                                    ('Perfecto simili-cuir', 'Look rock', 'Stradivarius', 69.99, 'thumbs/veste4.jpg', 'FEMME', 'S', 3);

-- Baskets (category_id = 4)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Air Max 90', 'Confort et style', 'Nike', 129.99, 'thumbs/basket1.jpg', 'HOMME', 'POINTURE_42', 4),
                                                                                                    ('Superstar', 'Classique Adidas', 'Adidas', 99.90, 'thumbs/basket2.jpg', 'FEMME', 'POINTURE_39', 4),
                                                                                                    ('Chuck Taylor', 'Style vintage', 'Converse', 74.95, 'thumbs/basket3.jpg', 'FEMME', 'POINTURE_38', 4),
                                                                                                    ('Air Force 1', 'Modèle emblématique', 'Nike', 119.00, 'thumbs/basket4.jpg', 'HOMME', 'POINTURE_40', 4);

-- Bottes (category_id = 5)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Bottes en cuir noir', 'Hiver élégant', 'Timberland', 149.90, 'thumbs/bottes1.jpg', 'HOMME', 'POINTURE_42', 5),
                                                                                                    ('Bottines talon carré', 'Style chic', 'Zara', 69.99, 'thumbs/bottes2.jpg', 'FEMME', 'POINTURE_38', 5),
                                                                                                    ('Bottes fourrées', 'Grand froid', 'UGG', 179.00, 'thumbs/bottes3.jpg', 'FEMME', 'POINTURE_40', 5),
                                                                                                    ('Bottes motard', 'Look rebelle', 'Sandro', 139.99, 'thumbs/bottes4.jpg', 'HOMME', 'POINTURE_41', 5);

-- Sandales (category_id = 6)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Sandales cuir plates', 'Légères et confortables', 'H&M', 24.90, 'thumbs/sandales1.jpg', 'FEMME', 'POINTURE_38', 6),
                                                                                                    ('Tongs plage', 'Pour l’été', 'Quiksilver', 14.50, 'thumbs/sandales2.jpg', 'HOMME', 'POINTURE_41', 6),
                                                                                                    ('Sandales à talons', 'Élégantes pour soirées', 'Mango', 49.90, 'thumbs/sandales3.jpg', 'FEMME', 'POINTURE_39', 6),
                                                                                                    ('Claquettes sport', 'Pratiques et tendance', 'Nike', 29.90, 'thumbs/sandales4.jpg', 'HOMME', 'POINTURE_40', 6);

-- Chemises (category_id = 7)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Chemise blanche', 'Classique intemporel', 'Celio', 35.00, 'thumbs/chemise1.jpg', 'HOMME', 'L', 7),
                                                                                                    ('Chemise fluide', 'Tissu léger', 'Zara', 39.90, 'thumbs/chemise2.jpg', 'FEMME', 'M', 7),
                                                                                                    ('Chemise imprimée', 'Style vacances', 'H&M', 24.99, 'thumbs/chemise3.jpg', 'HOMME', 'XL', 7),
                                                                                                    ('Chemise oversize', 'Tendance urbaine', 'Pull&Bear', 29.90, 'thumbs/chemise4.jpg', 'FEMME', 'S', 7);

-- Pulls (category_id = 8)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Pull col roulé', 'Chaud et stylé', 'Uniqlo', 39.99, 'thumbs/pull1.jpg', 'HOMME', 'M', 8),
                                                                                                    ('Pull en laine', 'Coupe droite', 'Mango', 49.90, 'thumbs/pull2.jpg', 'FEMME', 'L', 8),
                                                                                                    ('Sweat à capuche', 'Modèle classique', 'Nike', 59.90, 'thumbs/pull3.jpg', 'HOMME', 'XL', 8),
                                                                                                    ('Pull crop', 'Tendance jeune', 'Stradivarius', 34.90, 'thumbs/pull4.jpg', 'FEMME', 'S', 8);

-- Robes (category_id = 9)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Robe longue fleurie', 'Parfaite pour l’été', 'Zara', 59.99, 'thumbs/robe1.jpg', 'FEMME', 'M', 9),
                                                                                                    ('Robe portefeuille', 'Coupe flatteuse', 'Mango', 69.90, 'thumbs/robe2.jpg', 'FEMME', 'L', 9),
                                                                                                    ('Robe pull', 'Confort et chaleur', 'H&M', 39.90, 'thumbs/robe3.jpg', 'FEMME', 'S', 9),
                                                                                                    ('Robe satinée', 'Effet élégant', 'Stradivarius', 79.90, 'thumbs/robe4.jpg', 'FEMME', 'M', 9);

-- Accessoires (category_id = 10)
INSERT INTO product (nom, description, marque, prix, thumbnail_url, genre, taille, category_id) VALUES
                                                                                                    ('Ceinture en cuir', 'Boucle argentée', 'Celio', 19.99, 'thumbs/accessoire1.jpg', 'HOMME', 'M', 10),
                                                                                                    ('Sac à main noir', 'Petit format chic', 'Zara', 49.90, 'thumbs/accessoire2.jpg', 'FEMME', 'M', 10),
                                                                                                    ('Casquette street', 'Style urbain', 'New Era', 29.90, 'thumbs/accessoire3.jpg', 'HOMME', 'L', 10),
                                                                                                    ('Bonnet tricoté', 'Pour l’hiver', 'H&M', 14.99, 'thumbs/accessoire4.jpg', 'FEMME', 'S', 10);
