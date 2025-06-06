// src/models/Product.js

export default class Product {
    constructor({ id, nom, description, marque, prix, thumbnailUrl, genre, taille, category }) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.marque = marque;
        this.prix = prix;
        this.thumbnailUrl = thumbnailUrl;
        this.genre = genre;
        this.taille = taille;
        this.category = category; // Cet objet sera l'objet CategoryEntity brut si renvoyé par le backend
    }

    /**
     * Crée une nouvelle instance de Product à partir d'un objet de données.
     * Cette méthode est utile pour instancier un produit à partir de données brutes,
     * par exemple, celles que vous pourriez vouloir créer ou modifier côté client.
     *
     * @param {Object} data - L'objet de données avec les propriétés du produit.
     * @param {number} data.id
     * @param {string} data.nom
     * @param {string} [data.description]
     * @param {string} [data.marque]
     * @param {number} [data.prix]
     * @param {string} [data.thumbnailUrl]
     * @param {string} [data.genre]
     * @param {string} [data.taille]
     * @param {Object} [data.category] - L'objet CategoryEntity ou juste un ID.
     * @returns {Product} Une nouvelle instance de Product.
     */
    static build({ id, nom, description, marque, prix, thumbnailUrl, genre, taille, category }) {
        return new Product({ id, nom, description, marque, prix, thumbnailUrl, genre, taille, category });
    }

    /**
     * Analyse et transforme une valeur reçue (généralement du backend) en une instance de Product.
     * Cette méthode est utile pour s'assurer que les données brutes de l'API sont bien
     * transformées en instances de notre classe Product, si nécessaire.
     *
     * @param {Object | null} value - L'objet produit brut reçu de l'API ou null.
     * @returns {Product | null} Une instance de Product si la valeur est valide, sinon null.
     */
    static parse(value) {
        if (!value) {
            return null;
        }
        return Product.build({
            id: value.id,
            nom: value.nom,
            description: value.description,
            marque: value.marque,
            prix: value.prix,
            thumbnailUrl: value.thumbnailUrl,
            genre: value.genre,
            taille: value.taille,
            category: value.category? value.category:null // Assurez-vous que l'objet category est bien passé s'il est présent
        });
    }

    // --- Méthodes utilitaires (facultatif mais utile) ---

    /**
     * Retourne le prix formaté en euros.
     * @returns {string} Le prix formaté.
     */
    get formattedPrice() {
        return this.prix !== undefined && this.prix !== null ? `${this.prix.toFixed(2)} €` : 'N/A';
    }

    /**
     * Vérifie si le produit a une URL de miniature valide.
     * @returns {boolean} True si thumbnailUrl est non nul et non vide.
     */
    hasThumbnail() {
        return this.thumbnailUrl !== null && this.thumbnailUrl !== '';
    }

    /**
     * Convertit l'instance de produit en un objet "plain" JavaScript.
     * Utile si vous devez envoyer l'objet vers une API qui n'attend pas d'instances de classe.
     * @returns {Object} Un objet simple représentant le produit.
     */
    toPlainObject() {
        return {
            id: this.id,
            nom: this.nom,
            description: this.description,
            marque: this.marque,
            prix: this.prix,
            thumbnailUrl: this.thumbnailUrl,
            genre: this.genre,
            taille: this.taille,
            category: this.category // Si category est un objet, il sera aussi inclus
        };
    }
}