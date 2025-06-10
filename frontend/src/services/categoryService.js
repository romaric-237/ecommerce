// src/services/categoryService.js
import axios from 'axios';
import Category from "@/models/Category.js";

const API_BASE_URL = 'http://localhost:8080/api';

const categoryService = {
    /**
     * Récupère toutes les catégories.
     * @returns {Promise<Array>} Une promesse qui résout en une liste de catégories.
     */
    async getAllCategories() {
        try {
            const response = await axios.get(`${API_BASE_URL}/categories`);
            return response.data.map(category => Category.parse(category));
        } catch (error) {
            console.error("Erreur lors de la récupération des catégories :", error);
            throw error;
        }
    }
};

export default categoryService;