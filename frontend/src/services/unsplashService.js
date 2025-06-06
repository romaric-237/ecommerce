// src/services/unsplashService.js
import axios from 'axios';

const UNSPLASH_ACCESS_KEY = 'J326BByVX1WHRsrH4UnBCn-7UNAACqY3Ln66ycLjXSY'; // <-- REMPLACEZ CECI

const UNSPLASH_API_URL = 'https://api.unsplash.com';

const unsplashService = {
    /**
     * Cherche des images sur Unsplash.
     * @param {string} query - Le mot-clé de recherche (ex: "chaussures", "ordinateur").
     * @param {number} [perPage=1] - Le nombre d'images à récupérer (par défaut: 1).
     * @returns {Promise<string|null>} - L'URL de la première image trouvée, ou null.
     */
    async searchImage(query, perPage = 1) {
        if (!query || !UNSPLASH_ACCESS_KEY) {
            console.warn('Unsplash API key ou query manquant.');
            return null;
        }

        try {
            const response = await axios.get(`${UNSPLASH_API_URL}/search/photos`, {
                params: {
                    query: query,
                    per_page: perPage,
                    // Vous pouvez ajouter d'autres paramètres comme 'orientation=squarish'
                    // 'client_id' est généralement suffisant pour les requêtes non authentifiées de base.
                    client_id: UNSPLASH_ACCESS_KEY
                }
            });

            if (response.data.results && response.data.results.length > 0) {
                // Retourne l'URL de la petite taille (small) ou regular, selon votre besoin
                return response.data.results[0].urls.regular; // ou .small, .thumb
            } else {
                console.log(`Aucune image trouvée pour la recherche : "${query}"`);
                return null;
            }
        } catch (error) {
            console.error(`Erreur lors de la recherche d'image sur Unsplash pour "${query}" :`, error);
            return null;
        }
    }
};

export default unsplashService;