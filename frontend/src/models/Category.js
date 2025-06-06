// src/models/Category.js

export default class Category {
    constructor({ id, nom, description, produitentity }) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        // produitentity sera une liste d'objets ProductEntity bruts ou null/undefined.
        // Il est rarement utile d'instancier des Product dans le constructeur de Category
        // car la relation est souvent chargée paresseusement côté frontend ou pas du tout pour des raisons de performance.
        this.produitentity = produitentity;
    }

    /**
     * Crée une nouvelle instance de Category à partir d'un objet de données.
     *
     * @param {Object} data - L'objet de données avec les propriétés de la catégorie.
     * @param {number} data.id
     * @param {string} data.nom
     * @param {string} [data.description]
     * @param {Array<Object>} [data.produitentity] - Liste des produits associés (peut être omise ou vide).
     * @returns {Category} Une nouvelle instance de Category.
     */
    static build({ id, nom, description, produitentity }) {
        return new Category({ id, nom, description, produitentity });
    }

    /**
     * Analyse et transforme une valeur reçue (généralement du backend) en une instance de Category.
     *
     * @param {Object | null} value - L'objet catégorie brut reçu de l'API ou null.
     * @returns {Category | null} Une instance de Category si la valeur est valide, sinon null.
     */
    static parse(value) {
        if (!value) {
            return null;
        }
        return Category.build({
            id: value.id,
            nom: value.nom,
            description: value.description,
            // Note: produitentity peut être inclus ou non selon votre configuration @JsonManagedReference
            // et les paramètres de votre requête API.
            // Si vous le recevez et que vous voulez le transformer en instances de Product,
            // vous devrez importer la classe Product et faire un map ici.
            // Ex: produitentity: value.produitentity ? value.produitentity.map(p => Product.parse(p)) : []
            produitentity: value.produitentity // Conserve la liste telle quelle, ou transformez-la si nécessaire
        });
    }

    // --- Méthodes utilitaires (facultatif mais utile) ---

    /**
     * Vérifie si la catégorie a une description.
     * @returns {boolean} True si la description est non nulle et non vide.
     */
    hasDescription() {
        return this.description !== null && this.description !== '';
    }

    /**
     * Retourne le nom de la catégorie en majuscules.
     * @returns {string} Le nom de la catégorie en majuscules.
     */
    get upperCaseNom() {
        return this.nom ? this.nom.toUpperCase() : '';
    }

    /**
     * Convertit l'instance de catégorie en un objet "plain" JavaScript.
     * Utile si vous devez envoyer l'objet vers une API (par exemple, pour créer/modifier une catégorie).
     * @returns {Object} Un objet simple représentant la catégorie.
     */
    toPlainObject() {
        return {
            id: this.id,
            nom: this.nom,
            description: this.description,
            // produits: this.produits ? this.produits.map(p => p.toPlainObject()) : [] // Si vous voulez inclure les produits sérialisés
        };
    }
}