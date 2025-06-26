// src/services/productService.js
import {EcommerceApi} from './http-common.js';
import Product from "@/models/Product.js";

const API_BASE_URL = 'http://localhost:8080/api';

const productService = {
    /**
     * Récupère tous les produits.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de produits.
     */
    async getAllProducts() {
        try {
            const response = await EcommerceApi.get('/products');
            console.log('Réponse API produits:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où il n'y a pas de contenu (status 204) ou data est null/undefined
            if (!response.data || !Array.isArray(response.data)) {
                console.log('Aucun produit trouvé ou réponse vide');
                return [];
            }
            
            return response.data.map(product => Product.parse(product));
        } catch (error) {
            console.error("Erreur lors de la récupération de tous les produits :", error);
            // En cas d'erreur, retourner un tableau vide plutôt que de propager l'erreur
            return [];
        }
    },

    /**
     * Récupère les produits d'une catégorie spécifique.
     * @param {number} categoryId L'ID de la catégorie.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de produits de la catégorie.
     */
    async getProductsByCategory(categoryId) {
        try {
            const response = await EcommerceApi.get(`/products/category/${categoryId}`);
            console.log('Réponse API produits par catégorie:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où il n'y a pas de contenu (status 204) ou data est null/undefined
            if (!response.data || !Array.isArray(response.data)) {
                console.log(`Aucun produit trouvé pour la catégorie ${categoryId}`);
                return [];
            }
            
            return response.data.map(product => Product.parse(product));
        } catch (error) {
            console.error(`Erreur lors de la récupération des produits de la catégorie ${categoryId} :`, error);
            // En cas d'erreur, retourner un tableau vide plutôt que de propager l'erreur
            return [];
        }
    },

    async getProductById(productId) {
        try {
            const response = await EcommerceApi.get(`/products/${productId}`);
            console.log('Réponse API produit par ID:', response.data, 'Status:', response.status); // Pour le débogage
            
            // Gérer le cas où le produit n'existe pas
            if (!response.data) {
                console.log(`Produit ${productId} non trouvé`);
                return null;
            }
            
            return Product.parse(response.data);
        } catch (error) {
            console.error(`Erreur lors de la récupération du produit ${productId} :`, error);
            
            // Si c'est une erreur 404, retourner null au lieu de propager l'erreur
            if (error.response?.status === 404) {
                return null;
            }
            
            throw error;
        }
    },

    /**
     * Met à jour un produit (réservé aux gestionnaires)
     * @param {number} productId L'ID du produit à mettre à jour
     * @param {Object} productData Les nouvelles données du produit
     * @returns {Promise<Object>} Une promesse qui résout en le produit mis à jour
     */
    async updateProduct(productId, productData) {
        try {
            console.log('Tentative de mise à jour du produit:', productId, productData);
            
            const response = await EcommerceApi.put(`/products/update/${productId}`, productData);
            console.log('Produit mis à jour avec succès:', response.data);
            
            return response.data;
        } catch (error) {
            console.error(`Erreur lors de la mise à jour du produit ${productId}:`, error);
            
            // Gérer les erreurs spécifiques
            if (error.response?.status === 403) {
                throw new Error('Accès refusé. Vous devez être gestionnaire pour modifier les produits.');
            } else if (error.response?.status === 404) {
                throw new Error('Produit non trouvé.');
            } else if (error.response?.status === 400) {
                const errorMessage = error.response.data?.message || 'Données invalides';
                throw new Error(`Erreur de validation: ${errorMessage}`);
            }
            
            throw new Error('Erreur lors de la mise à jour du produit. Veuillez réessayer.');
        }
    },

    /**
     * Met à jour un produit via l'endpoint admin (alternative)
     * @param {number} productId L'ID du produit à mettre à jour
     * @param {Object} productData Les nouvelles données du produit
     * @returns {Promise<Object>} Une promesse qui résout en le produit mis à jour
     */
    async updateProductAdmin(productId, productData) {
        try {
            console.log('Tentative de mise à jour du produit (admin):', productId, productData);
            
            const response = await EcommerceApi.put(`/admin/products/${productId}`, productData);
            console.log('Produit mis à jour avec succès (admin):', response.data);
            
            return response.data;
        } catch (error) {
            console.error(`Erreur lors de la mise à jour du produit ${productId} (admin):`, error);
            
            // Gérer les erreurs spécifiques
            if (error.response?.status === 403) {
                throw new Error('Accès refusé. Vous devez être gestionnaire pour accéder à cette fonctionnalité.');
            } else if (error.response?.status === 404) {
                throw new Error('Produit non trouvé.');
            } else if (error.response?.status === 400) {
                const errorMessage = error.response.data?.message || 'Données invalides';
                throw new Error(`Erreur de validation: ${errorMessage}`);
            }
            
            throw new Error('Erreur lors de la mise à jour du produit. Veuillez réessayer.');
        }
    },

    /**
     * Récupère tous les produits (version admin avec plus de détails)
     * @returns {Promise<Array>} Une promesse qui résout en une liste de produits
     */
    async getAllProductsAdmin() {
        try {
            const response = await EcommerceApi.get('/admin/products');
            console.log('Réponse API produits admin:', response.data, 'Status:', response.status);
            
            if (!response.data || !Array.isArray(response.data)) {
                console.log('Aucun produit trouvé (admin)');
                return [];
            }
            
            return response.data;
        } catch (error) {
            console.error("Erreur lors de la récupération des produits (admin):", error);
            
            if (error.response?.status === 403) {
                throw new Error('Accès refusé. Vous devez être gestionnaire pour accéder à cette fonctionnalité.');
            }
            
            return [];
        }
    }
};

export default productService;