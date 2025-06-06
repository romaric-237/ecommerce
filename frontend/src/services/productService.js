// src/services/productService.js
import axios from 'axios';
import Product from "@/models/Product.js";


const API_BASE_URL = 'http://localhost:9494/api';

const productService = {
    /**
     * Récupère tous les produits.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de produits.
     */
    async getAllProducts() {
        try {
            const response = await axios.get(`${API_BASE_URL}/products`);
            return response.data.map(product => Product.parse(product));
        } catch (error) {
            console.error("Erreur lors de la récupération de tous les produits :", error);
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
            const response = await axios.get(`${API_BASE_URL}/products/category/${categoryId}`);
            return response.data.map(product => Product.parse(product));
        } catch (error) {
            console.error(`Erreur lors de la récupération des produits de la catégorie ${categoryId} :`, error);
            throw error;
        }
    },

    async getProductById(productId) {
      try {
        const response = await axios.get(`${API_BASE_URL}/products/${productId}`);
        return response.data;
      } catch (error) {
        console.error(`Erreur lors de la récupération du produit ${productId} :`, error);
        throw error;
      }
    }
};

export default productService;