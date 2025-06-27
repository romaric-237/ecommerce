import EcommerceApi from './http-common';

const orderService = {
    /**
     * Crée une nouvelle commande
     * @param {Object} orderData - Données de la commande
     * @returns {Promise<Object>} La commande créée
     */
    async createOrder(orderData) {
        try {
            console.log('Création de commande:', orderData);
            const response = await EcommerceApi.post('/orders', orderData);
            console.log('Commande créée:', response.data);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la création de la commande:', error);
            throw error;
        }
    },

    /**
     * Récupère toutes les commandes de l'utilisateur connecté
     * @returns {Promise<Array>} Liste des commandes
     */
    async getMyOrders() {
        try {
            const response = await EcommerceApi.get('/orders/my-orders');
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la récupération des commandes:', error);
            throw error;
        }
    },

    /**
     * Récupère une commande par son ID
     * @param {number} orderId - ID de la commande
     * @returns {Promise<Object>} La commande
     */
    async getOrderById(orderId) {
        try {
            const response = await EcommerceApi.get(`/orders/${orderId}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la récupération de la commande:', error);
            throw error;
        }
    },

    /**
     * Récupère une commande par son numéro
     * @param {string} orderNumber - Numéro de la commande
     * @returns {Promise<Object>} La commande
     */
    async getOrderByNumber(orderNumber) {
        try {
            const response = await EcommerceApi.get(`/orders/number/${orderNumber}`);
            return response.data;
        } catch (error) {
            console.error('Erreur lors de la récupération de la commande:', error);
            throw error;
        }
    }
};

export default orderService; 