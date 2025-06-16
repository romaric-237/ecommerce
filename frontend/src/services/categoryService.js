// src/services/categoryService.js
import {EcommerceApi} from './http-common.js';
import Category from "@/models/Category.js";

const categoryService = {
    /**
     * Récupère toutes les catégories.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de catégories.
     */
    async getAllCategories() {
        try {
            const response = await EcommerceApi.get('/categories');
            console.log('Réponse API catégories:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où il n'y a pas de contenu (status 204) ou data est null/undefined
            if (!response.data || !Array.isArray(response.data)) {
                console.log('Aucune catégorie trouvée ou réponse vide');
                return [];
            }
            
            return response.data.map(category => Category.parse(category));
        } catch (error) {
            console.error("Erreur lors de la récupération des catégories :", error);
            // En cas d'erreur, retourner un tableau vide plutôt que de propager l'erreur
            return [];
        }
    },

    /**
     * Récupère une catégorie par son ID.
     * @param {number} categoryId L'ID de la catégorie.
     * @returns {Promise<Category|null>} Une promesse qui résout en une catégorie ou null si non trouvée.
     */
    async getCategoryById(categoryId) {
        try {
            const response = await EcommerceApi.get(`/category/${categoryId}`);
            console.log('Réponse API catégorie par ID:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où la catégorie n'existe pas
            if (!response.data) {
                console.log(`Catégorie ${categoryId} non trouvée`);
                return null;
            }
            
            return Category.parse(response.data);
        } catch (error) {
            console.error(`Erreur lors de la récupération de la catégorie ${categoryId} :`, error);
            
            // Si c'est une erreur 404, retourner null au lieu de propager l'erreur
            if (error.response?.status === 404) {
                return null;
            }
            
            throw error;
        }
    },

    /**
     * Récupère les produits d'une catégorie spécifique.
     * @param {number} categoryId L'ID de la catégorie.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de produits de la catégorie.
     */
    async getProductsByCategory(categoryId) {
        try {
            const response = await EcommerceApi.get(`/category/${categoryId}/products`);
            console.log('Réponse API produits par catégorie:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où il n'y a pas de contenu (status 204) ou data est null/undefined
            if (!response.data || !Array.isArray(response.data)) {
                console.log(`Aucun produit trouvé pour la catégorie ${categoryId}`);
                return [];
            }
            
            // Note: On retourne les données brutes car c'est un service de catégories
            // qui peut être utilisé avec ProductDTO dans d'autres contextes
            return response.data;
        } catch (error) {
            console.error(`Erreur lors de la récupération des produits de la catégorie ${categoryId} :`, error);
            // En cas d'erreur, retourner un tableau vide plutôt que de propager l'erreur
            return [];
        }
    },

    /**
     * Recherche des catégories par nom.
     * @param {string} searchTerm Le terme de recherche.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de catégories correspondantes.
     */
    async searchCategories(searchTerm) {
        try {
            if (!searchTerm || searchTerm.trim().length < 2) {
                console.log('Terme de recherche trop court');
                return [];
            }
            
            const response = await EcommerceApi.get(`/categories/search?q=${encodeURIComponent(searchTerm.trim())}`);
            console.log('Réponse API recherche catégories:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où il n'y a pas de contenu (status 204) ou data est null/undefined
            if (!response.data || !Array.isArray(response.data)) {
                console.log(`Aucune catégorie trouvée pour la recherche: ${searchTerm}`);
                return [];
            }
            
            return response.data.map(category => Category.parse(category));
        } catch (error) {
            console.error(`Erreur lors de la recherche de catégories avec le terme ${searchTerm} :`, error);
            // En cas d'erreur, retourner un tableau vide plutôt que de propager l'erreur
            return [];
        }
    }
};

export default categoryService;
