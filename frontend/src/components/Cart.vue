<template>
  <div class="cart-container">
    <!-- Bouton pour ouvrir le panier -->
    <div class="cart-trigger" @click="cartStore.openCart">
      <i class="fas fa-shopping-cart"></i>
      <span v-if="cartStore.totalItems > 0" class="cart-badge">
        {{ cartStore.totalItems }}
      </span>
    </div>

    <!-- Modal du panier -->
    <div v-if="cartStore.isCartOpen" class="cart-modal-overlay" @click="cartStore.closeCart">
      <div class="cart-modal" @click.stop>
        <div class="cart-header">
          <h3>Mon Panier</h3>
          <button class="close-btn" @click="cartStore.closeCart">
            <i class="fas fa-times"></i>
          </button>
        </div>

        <div class="cart-content">
          <!-- Message si panier vide -->
          <div v-if="cartStore.isEmpty" class="empty-cart">
            <i class="fas fa-shopping-basket"></i>
            <p>Votre panier est vide</p>
            <button class="btn-continue-shopping" @click="cartStore.closeCart">
              Continuer mes achats
            </button>
          </div>

          <!-- Liste des produits dans le panier -->
          <div v-else class="cart-items">
            <div v-for="item in cartStore.items" :key="item.id" class="cart-item">
              <div class="item-image">
                <img :src="item.image" :alt="item.name" />
              </div>
              
              <div class="item-details">
                <h4 class="item-name" @click="viewProduct(item.id)">
                  {{ item.name }}
                </h4>
                <p class="item-brand">{{ item.brand }}</p>
                <p class="item-price">{{ formatPrice(item.price) }} €</p>
              </div>

              <div class="item-quantity">
                <button 
                  class="quantity-btn" 
                  @click="cartStore.updateQuantity(item.id, item.quantity - 1)"
                  :disabled="item.quantity <= 1"
                >
                  <i class="fas fa-minus"></i>
                </button>
                <span class="quantity">{{ item.quantity }}</span>
                <button 
                  class="quantity-btn" 
                  @click="cartStore.updateQuantity(item.id, item.quantity + 1)"
                >
                  <i class="fas fa-plus"></i>
                </button>
              </div>

              <div class="item-total">
                <p class="total-price">{{ formatPrice(item.price * item.quantity) }} €</p>
              </div>

              <div class="item-actions">
                <button 
                  class="remove-btn" 
                  @click="cartStore.removeFromCart(item.id)"
                  title="Retirer du panier"
                >
                  <i class="fas fa-trash"></i>
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- Footer du panier avec total -->
        <div v-if="!cartStore.isEmpty" class="cart-footer">
          <div class="cart-total">
            <span class="total-label">Total :</span>
            <span class="total-amount">{{ cartStore.formattedTotalPrice }}</span>
          </div>
          
          <div class="cart-actions">
            <button class="btn-clear-cart" @click="clearCart">
              Vider le panier
            </button>
            <button class="btn-checkout" @click="proceedToCheckout">
              Passer la commande
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useCartStore } from '@/stores/cart'
import { useRouter } from 'vue-router'

export default {
  name: 'Cart',
  setup() {
    const cartStore = useCartStore()
    const router = useRouter()

    // Charger le panier (depuis l'API si connecté, sinon localStorage)
    cartStore.loadCart()

    const formatPrice = (price) => {
      return new Intl.NumberFormat('fr-FR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }).format(price)
    }

    const viewProduct = (productId) => {
      cartStore.closeCart()
      router.push(`/products/${productId}`)
    }

    const clearCart = () => {
      if (confirm('Êtes-vous sûr de vouloir vider votre panier ?')) {
        cartStore.clearCart()
      }
    }

    const proceedToCheckout = () => {
      cartStore.closeCart()
      router.push('/checkout')
    }

    return {
      cartStore,
      formatPrice,
      viewProduct,
      clearCart,
      proceedToCheckout
    }
  }
}
</script>

<style scoped>
.cart-container {
  position: relative;
}

.cart-trigger {
  position: relative;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 6px;
  transition: background-color 0.3s ease;
  display: flex;
  align-items: center;
  gap: 8px;
}

.cart-trigger:hover {
  background-color: rgba(0, 123, 255, 0.1);
}

.cart-trigger i {
  font-size: 1.2em;
  color: #007bff;
}

.cart-badge {
  background-color: #dc3545;
  color: white;
  border-radius: 50%;
  padding: 2px 6px;
  font-size: 0.75em;
  font-weight: bold;
  min-width: 18px;
  text-align: center;
}

.cart-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: flex-start;
  z-index: 1000;
  padding: 20px;
}

.cart-modal {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  width: 100%;
  max-width: 600px;
  max-height: 80vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.cart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
  background-color: #f8f9fa;
}

.cart-header h3 {
  margin: 0;
  color: #333;
  font-size: 1.5em;
}

.close-btn {
  background: none;
  border: none;
  font-size: 1.2em;
  cursor: pointer;
  color: #666;
  padding: 5px;
  border-radius: 50%;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background-color: #e9ecef;
}

.cart-content {
  flex: 1;
  overflow-y: auto;
  padding: 20px;
}

.empty-cart {
  text-align: center;
  padding: 40px 20px;
  color: #666;
}

.empty-cart i {
  font-size: 3em;
  margin-bottom: 20px;
  color: #ccc;
}

.empty-cart p {
  font-size: 1.2em;
  margin-bottom: 20px;
}

.btn-continue-shopping {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
}

.btn-continue-shopping:hover {
  background-color: #0056b3;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.cart-item {
  display: grid;
  grid-template-columns: 80px 1fr auto auto auto;
  gap: 15px;
  align-items: center;
  padding: 15px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #fafafa;
}

.item-image img {
  width: 80px;
  height: 80px;
  object-fit: cover;
  border-radius: 6px;
}

.item-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.item-name {
  margin: 0;
  font-size: 1.1em;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  transition: color 0.3s ease;
}

.item-name:hover {
  color: #007bff;
}

.item-brand {
  margin: 0;
  font-size: 0.9em;
  color: #666;
}

.item-price {
  margin: 0;
  font-weight: 600;
  color: #28a745;
}

.item-quantity {
  display: flex;
  align-items: center;
  gap: 10px;
}

.quantity-btn {
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.quantity-btn:hover:not(:disabled) {
  background-color: #e9ecef;
}

.quantity-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.quantity {
  font-weight: 600;
  min-width: 20px;
  text-align: center;
}

.item-total {
  text-align: right;
}

.total-price {
  margin: 0;
  font-weight: 700;
  font-size: 1.1em;
  color: #28a745;
}

.item-actions {
  display: flex;
  justify-content: center;
}

.remove-btn {
  background-color: #dc3545;
  color: white;
  border: none;
  border-radius: 4px;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.remove-btn:hover {
  background-color: #c82333;
}

.cart-footer {
  padding: 20px;
  border-top: 1px solid #eee;
  background-color: #f8f9fa;
}

.cart-total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.total-label {
  font-size: 1.2em;
  font-weight: 600;
  color: #333;
}

.total-amount {
  font-size: 1.5em;
  font-weight: 700;
  color: #28a745;
}

.cart-actions {
  display: flex;
  gap: 10px;
}

.btn-clear-cart {
  flex: 1;
  background-color: #6c757d;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
}

.btn-clear-cart:hover {
  background-color: #5a6268;
}

.btn-checkout {
  flex: 2;
  background-color: #28a745;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  font-weight: 600;
  transition: background-color 0.3s ease;
}

.btn-checkout:hover {
  background-color: #218838;
}

/* Responsive */
@media (max-width: 768px) {
  .cart-modal {
    max-width: 95vw;
    margin: 10px;
  }

  .cart-item {
    grid-template-columns: 60px 1fr;
    gap: 10px;
  }

  .item-image img {
    width: 60px;
    height: 60px;
  }

  .item-quantity,
  .item-total,
  .item-actions {
    grid-column: 2;
    justify-self: start;
  }

  .cart-actions {
    flex-direction: column;
  }
}
</style> 