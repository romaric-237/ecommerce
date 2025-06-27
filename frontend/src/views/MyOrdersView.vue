<template>
  <div class="my-orders-container">
    <div class="orders-header">
      <h1>Mes Commandes</h1>
      <p class="orders-subtitle">Retrouvez l'historique de toutes vos commandes</p>
    </div>

    <!-- État de chargement -->
    <div v-if="loading" class="loading-container">
      <div class="spinner"></div>
      <p>Chargement de vos commandes...</p>
    </div>

    <!-- Message si aucune commande -->
    <div v-else-if="orders.length === 0" class="empty-state">
      <div class="empty-icon">📦</div>
      <h3>Aucune commande pour le moment</h3>
      <p>Vous n'avez pas encore passé de commande. Découvrez nos produits !</p>
      <router-link to="/products" class="btn-primary">
        Voir les produits
      </router-link>
    </div>

    <!-- Liste des commandes -->
    <div v-else class="orders-list">
      <div class="orders-stats">
        <div class="stat-card">
          <span class="stat-number">{{ orders.length }}</span>
          <span class="stat-label">Commandes totales</span>
        </div>
        <div class="stat-card">
          <span class="stat-number">{{ formatPrice(totalSpent) }}</span>
          <span class="stat-label">Total dépensé</span>
        </div>
      </div>

      <div class="orders-grid">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <div class="order-info">
              <h3 class="order-number">Commande #{{ order.orderNumber }}</h3>
              <p class="order-date">{{ formatDate(order.orderDate) }}</p>
            </div>
            <div class="order-status">
              <span :class="statusClass(order.status)" class="status-badge">
                {{ statusLabel(order.status) }}
              </span>
            </div>
          </div>

          <div class="order-details">
            <div class="order-items">
              <div v-for="item in order.orderItems" :key="item.id" class="order-item">
                <img :src="item.productImage" :alt="item.productName" class="item-image" />
                <div class="item-info">
                  <h4 class="item-name">{{ item.productName }}</h4>
                  <p class="item-brand">{{ item.productBrand }}</p>
                  <p class="item-quantity">Quantité: {{ item.quantity }}</p>
                </div>
                <div class="item-price">
                  {{ formatPrice(item.totalPrice) }}
                </div>
              </div>
            </div>

            <div class="order-summary">
              <div class="summary-row">
                <span>Total commande:</span>
                <span class="total-amount">{{ formatPrice(order.totalAmount) }}</span>
              </div>
              <div class="summary-row">
                <span>Méthode de paiement:</span>
                <span>{{ paymentMethodLabel(order.paymentMethod) }}</span>
              </div>
              <div class="summary-row">
                <span>Adresse de livraison:</span>
                <span>{{ order.shippingAddress }}, {{ order.shippingCity }}</span>
              </div>
            </div>
          </div>

          <div class="order-actions">
            <button class="btn-detail" @click="viewOrderDetail(order.id)">
              Voir les détails
            </button>
            <button v-if="order.status === 'PENDING'" class="btn-cancel" @click="cancelOrder(order.id)">
              Annuler
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import orderService from '@/services/orderService';

export default {
  name: 'MyOrdersView',
  data() {
    return {
      orders: [],
      loading: true,
      error: null
    };
  },
  computed: {
    totalSpent() {
      return this.orders.reduce((total, order) => total + order.totalAmount, 0);
    }
  },
  async created() {
    await this.loadOrders();
  },
  methods: {
    async loadOrders() {
      try {
        this.loading = true;
        this.orders = await orderService.getMyOrders();
      } catch (error) {
        console.error('Erreur lors du chargement des commandes:', error);
        this.error = 'Impossible de charger vos commandes. Veuillez réessayer.';
      } finally {
        this.loading = false;
      }
    },
    formatDate(date) {
      return new Date(date).toLocaleDateString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      });
    },
    formatPrice(price) {
      return new Intl.NumberFormat('fr-FR', { 
        style: 'currency', 
        currency: 'EUR' 
      }).format(price);
    },
    statusLabel(status) {
      const labels = {
        'PENDING': 'En attente de paiement',
        'PAID': 'Payée',
        'SHIPPED': 'Expédiée',
        'DELIVERED': 'Livrée',
        'CANCELLED': 'Annulée',
        'FAILED': 'Échec de paiement'
      };
      return labels[status] || status;
    },
    statusClass(status) {
      const classes = {
        'PAID': 'status-paid',
        'PENDING': 'status-pending',
        'SHIPPED': 'status-shipped',
        'DELIVERED': 'status-delivered',
        'CANCELLED': 'status-cancelled',
        'FAILED': 'status-failed'
      };
      return classes[status] || 'status-default';
    },
    paymentMethodLabel(method) {
      const labels = {
        'CREDIT_CARD': 'Carte de crédit',
        'DEBIT_CARD': 'Carte de débit',
        'BANK_TRANSFER': 'Virement bancaire',
        'PAYPAL': 'PayPal',
        'PAYSAFE_CARD': 'Paysafe Card'
      };
      return labels[method] || method;
    },
    viewOrderDetail(orderId) {
      // TODO: Implémenter la vue détaillée d'une commande
      console.log('Voir les détails de la commande:', orderId);
    },
    async cancelOrder(orderId) {
      if (confirm('Êtes-vous sûr de vouloir annuler cette commande ?')) {
        try {
          // TODO: Implémenter l'annulation de commande
          console.log('Annuler la commande:', orderId);
        } catch (error) {
          console.error('Erreur lors de l\'annulation:', error);
        }
      }
    }
  }
};
</script>

<style scoped>
.my-orders-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.orders-header {
  text-align: center;
  margin-bottom: 40px;
}

.orders-header h1 {
  color: #333;
  margin-bottom: 10px;
  font-size: 2.5em;
}

.orders-subtitle {
  color: #666;
  font-size: 1.1em;
}

.loading-container {
  text-align: center;
  padding: 60px 20px;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #007bff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.empty-state {
  text-align: center;
  padding: 60px 20px;
}

.empty-icon {
  font-size: 4em;
  margin-bottom: 20px;
}

.empty-state h3 {
  color: #333;
  margin-bottom: 10px;
}

.empty-state p {
  color: #666;
  margin-bottom: 30px;
}

.btn-primary {
  display: inline-block;
  padding: 12px 24px;
  background-color: #007bff;
  color: white;
  text-decoration: none;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.orders-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  text-align: center;
}

.stat-number {
  display: block;
  font-size: 2em;
  font-weight: bold;
  color: #007bff;
}

.stat-label {
  color: #666;
  font-size: 0.9em;
}

.orders-grid {
  display: grid;
  gap: 20px;
}

.order-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  overflow: hidden;
  transition: transform 0.3s, box-shadow 0.3s;
}

.order-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0,0,0,0.15);
}

.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f8f9fa;
  border-bottom: 1px solid #eee;
}

.order-number {
  margin: 0;
  color: #333;
  font-size: 1.2em;
}

.order-date {
  margin: 5px 0 0 0;
  color: #666;
  font-size: 0.9em;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.8em;
  font-weight: 600;
  text-transform: uppercase;
}

.status-paid { background: #d4edda; color: #155724; }
.status-pending { background: #fff3cd; color: #856404; }
.status-shipped { background: #d1ecf1; color: #0c5460; }
.status-delivered { background: #cce5ff; color: #004085; }
.status-cancelled { background: #e2e3e5; color: #383d41; }
.status-failed { background: #f8d7da; color: #721c24; }

.order-details {
  padding: 20px;
}

.order-items {
  margin-bottom: 20px;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px 0;
  border-bottom: 1px solid #f0f0f0;
}

.order-item:last-child {
  border-bottom: none;
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 8px;
}

.item-info {
  flex: 1;
}

.item-name {
  margin: 0 0 5px 0;
  font-size: 1em;
  color: #333;
}

.item-brand {
  margin: 0 0 5px 0;
  color: #666;
  font-size: 0.9em;
}

.item-quantity {
  margin: 0;
  color: #888;
  font-size: 0.8em;
}

.item-price {
  font-weight: 600;
  color: #28a745;
}

.order-summary {
  background: #f8f9fa;
  padding: 15px;
  border-radius: 8px;
}

.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
}

.summary-row:last-child {
  margin-bottom: 0;
}

.total-amount {
  font-weight: 600;
  color: #28a745;
}

.order-actions {
  display: flex;
  gap: 10px;
  padding: 20px;
  background: #f8f9fa;
  border-top: 1px solid #eee;
}

.btn-detail, .btn-cancel {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s;
}

.btn-detail {
  background: #007bff;
  color: white;
}

.btn-detail:hover {
  background: #0056b3;
}

.btn-cancel {
  background: #dc3545;
  color: white;
}

.btn-cancel:hover {
  background: #c82333;
}

@media (max-width: 768px) {
  .orders-stats {
    grid-template-columns: 1fr;
  }
  
  .order-header {
    flex-direction: column;
    gap: 10px;
    text-align: center;
  }
  
  .order-item {
    flex-direction: column;
    text-align: center;
  }
  
  .order-actions {
    flex-direction: column;
  }
}
</style> 