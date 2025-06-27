<template>
  <div class="checkout-container">
    <div class="checkout-header">
      <h1>Finaliser votre commande</h1>
      <p>Récapitulatif de votre panier et informations de paiement</p>
    </div>

    <div class="checkout-content">
      <!-- Récapitulatif du panier -->
      <div class="cart-summary">
        <h2>Récapitulatif du panier</h2>
        <div class="cart-items">
          <div v-for="item in cartStore.items" :key="item.id" class="cart-item">
            <div class="item-image">
              <img :src="item.image" :alt="item.name" />
            </div>
            <div class="item-details">
              <h4>{{ item.name }}</h4>
              <p class="item-brand">{{ item.brand }}</p>
              <p class="item-price">{{ formatPrice(item.price) }} €</p>
            </div>
            <div class="item-quantity">
              <span>Quantité: {{ item.quantity }}</span>
            </div>
            <div class="item-total">
              <span>{{ formatPrice(item.price * item.quantity) }} €</span>
            </div>
          </div>
        </div>
        
        <div class="cart-total">
          <h3>Total: {{ cartStore.formattedTotalPrice }}</h3>
        </div>
      </div>

      <!-- Formulaire de commande -->
      <div class="order-form">
        <h2>Informations de commande</h2>
        
        <!-- Adresse de livraison -->
        <div class="form-section">
          <h3>Adresse de livraison</h3>
          <div class="form-row">
            <div class="form-group">
              <label for="firstName">Prénom *</label>
              <input 
                type="text" 
                id="firstName" 
                v-model="orderForm.firstName" 
                required
              />
            </div>
            <div class="form-group">
              <label for="lastName">Nom *</label>
              <input 
                type="text" 
                id="lastName" 
                v-model="orderForm.lastName" 
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label for="address">Adresse *</label>
            <input 
              type="text" 
              id="address" 
              v-model="orderForm.address" 
              required
            />
          </div>
          
          <div class="form-row">
            <div class="form-group">
              <label for="city">Ville *</label>
              <input 
                type="text" 
                id="city" 
                v-model="orderForm.city" 
                required
              />
            </div>
            <div class="form-group">
              <label for="postalCode">Code postal *</label>
              <input 
                type="text" 
                id="postalCode" 
                v-model="orderForm.postalCode" 
                required
              />
            </div>
          </div>
          
          <div class="form-group">
            <label for="country">Pays *</label>
            <input 
              type="text" 
              id="country" 
              v-model="orderForm.country" 
              required
            />
          </div>
          
          <div class="form-group">
            <label for="phone">Téléphone *</label>
            <input 
              type="tel" 
              id="phone" 
              v-model="orderForm.phone" 
              required
            />
          </div>
        </div>

        <!-- Méthode de paiement -->
        <div class="form-section">
          <h3>Méthode de paiement</h3>
          <div class="payment-methods">
            <div 
              v-for="method in paymentMethods" 
              :key="method.value"
              class="payment-method"
              :class="{ selected: selectedPaymentMethod === method.value }"
              @click="selectedPaymentMethod = method.value"
            >
              <div class="payment-icon">
                <i :class="method.icon"></i>
              </div>
              <div class="payment-info">
                <h4>{{ method.label }}</h4>
                <p>{{ method.description }}</p>
              </div>
              <div class="payment-radio">
                <input 
                  type="radio" 
                  :value="method.value" 
                  v-model="selectedPaymentMethod"
                  :id="'payment-' + method.value"
                />
                <label :for="'payment-' + method.value"></label>
              </div>
            </div>
          </div>
        </div>

        <!-- Boutons d'action -->
        <div class="checkout-actions">
          <button 
            class="btn-back" 
            @click="goBack"
          >
            <i class="fas fa-arrow-left"></i>
            Retour au panier
          </button>
          
          <button 
            class="btn-confirm-order" 
            @click="confirmOrder"
            :disabled="!isFormValid || isProcessing"
          >
            <i class="fas fa-credit-card"></i>
            {{ isProcessing ? 'Traitement...' : 'Confirmer la commande' }}
          </button>
        </div>
      </div>
    </div>

    <!-- Modal de confirmation -->
    <div v-if="showConfirmationModal" class="modal-overlay" @click="closeConfirmationModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>Confirmation de commande</h3>
          <button class="close-btn" @click="closeConfirmationModal">
            <i class="fas fa-times"></i>
          </button>
        </div>
        <div class="modal-body">
          <div v-if="orderResult.success" class="success-message">
            <i class="fas fa-check-circle"></i>
            <h4>Commande confirmée !</h4>
            <p>Votre commande a été traitée avec succès.</p>
            <p><strong>Numéro de commande:</strong> {{ orderResult.orderNumber }}</p>
            <p><strong>Total:</strong> {{ orderResult.totalAmount }}</p>
          </div>
          <div v-else class="error-message">
            <i class="fas fa-exclamation-circle"></i>
            <h4>Erreur lors du traitement</h4>
            <p>{{ orderResult.message }}</p>
          </div>
        </div>
        <div class="modal-footer">
          <button 
            class="btn-primary" 
            @click="closeConfirmationModal"
          >
            {{ orderResult.success ? 'Retour à l\'accueil' : 'Réessayer' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useCartStore } from '@/stores/cart'
import orderService from '@/services/orderService'

export default {
  name: 'CheckoutView',
  setup() {
    const router = useRouter()
    const cartStore = useCartStore()
    
    // État du formulaire
    const orderForm = ref({
      firstName: '',
      lastName: '',
      address: '',
      city: '',
      postalCode: '',
      country: '',
      phone: ''
    })
    
    const selectedPaymentMethod = ref('')
    const isProcessing = ref(false)
    const showConfirmationModal = ref(false)
    const orderResult = ref({})
    
    // Méthodes de paiement disponibles
    const paymentMethods = ref([
      {
        value: 'PAYPAL',
        label: 'PayPal',
        description: 'Paiement sécurisé via PayPal',
        icon: 'fab fa-paypal'
      },
      {
        value: 'CREDIT_CARD',
        label: 'Carte de crédit/débit',
        description: 'Visa, Mastercard, American Express',
        icon: 'fas fa-credit-card'
      },
      {
        value: 'BANK_TRANSFER',
        label: 'Virement bancaire',
        description: 'Virement SEPA (2-3 jours ouvrables)',
        icon: 'fas fa-university'
      },
      {
        value: 'PAYSAFE',
        label: 'Paysafe Card',
        description: 'Paiement par carte prépayée',
        icon: 'fas fa-wallet'
      }
    ])
    
    // Validation du formulaire
    const isFormValid = computed(() => {
      return orderForm.value.firstName &&
             orderForm.value.lastName &&
             orderForm.value.address &&
             orderForm.value.city &&
             orderForm.value.postalCode &&
             orderForm.value.country &&
             orderForm.value.phone &&
             selectedPaymentMethod.value &&
             !cartStore.isEmpty
    })
    
    // Formatage des prix
    const formatPrice = (price) => {
      return new Intl.NumberFormat('fr-FR', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      }).format(price)
    }
    
    // Navigation
    const goBack = () => {
      router.go(-1)
    }
    
    // Confirmation de la commande
    const confirmOrder = async () => {
      if (!isFormValid.value) {
        alert('Veuillez remplir tous les champs obligatoires')
        return
      }
      
      isProcessing.value = true
      
      try {
        const orderData = {
          items: cartStore.items.map(item => ({
            productId: item.id,
            quantity: item.quantity,
            unitPrice: item.price
          })),
          shippingAddress: {
            firstName: orderForm.value.firstName,
            lastName: orderForm.value.lastName,
            address: orderForm.value.address,
            city: orderForm.value.city,
            postalCode: orderForm.value.postalCode,
            country: orderForm.value.country,
            phone: orderForm.value.phone
          },
          paymentMethod: selectedPaymentMethod.value,
          totalAmount: cartStore.totalPrice
        }
        
        const result = await orderService.createOrder(orderData)
        
        orderResult.value = {
          success: true,
          orderNumber: result.orderNumber,
          totalAmount: formatPrice(result.totalAmount) + ' €',
          message: 'Commande créée avec succès'
        }
        
        // Vider le panier après commande réussie
        cartStore.clearCart()
        
      } catch (error) {
        console.error('Erreur lors de la création de la commande:', error)
        orderResult.value = {
          success: false,
          message: error.response?.data?.message || 'Erreur lors du traitement de la commande'
        }
      } finally {
        isProcessing.value = false
        showConfirmationModal.value = true
      }
    }
    
    // Fermer le modal de confirmation
    const closeConfirmationModal = () => {
      showConfirmationModal.value = false
      if (orderResult.value.success) {
        router.push('/')
      }
    }
    
    // Vérifier que le panier n'est pas vide
    onMounted(() => {
      if (cartStore.isEmpty) {
        router.push('/')
      }
    })
    
    return {
      cartStore,
      orderForm,
      selectedPaymentMethod,
      paymentMethods,
      isFormValid,
      isProcessing,
      showConfirmationModal,
      orderResult,
      formatPrice,
      goBack,
      confirmOrder,
      closeConfirmationModal
    }
  }
}
</script>

<style scoped>
.checkout-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.checkout-header {
  text-align: center;
  margin-bottom: 40px;
}

.checkout-header h1 {
  color: #333;
  margin-bottom: 10px;
}

.checkout-header p {
  color: #666;
  font-size: 1.1em;
}

.checkout-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 40px;
}

.cart-summary {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  height: fit-content;
}

.cart-summary h2 {
  margin-bottom: 20px;
  color: #333;
}

.cart-items {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.cart-item {
  display: grid;
  grid-template-columns: 60px 1fr auto auto;
  gap: 15px;
  align-items: center;
  padding: 15px;
  background: white;
  border-radius: 6px;
  border: 1px solid #eee;
}

.item-image img {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.item-details h4 {
  margin: 0 0 5px 0;
  font-size: 1em;
}

.item-brand {
  margin: 0 0 5px 0;
  font-size: 0.9em;
  color: #666;
}

.item-price {
  margin: 0;
  font-weight: 600;
  color: #28a745;
}

.item-quantity {
  font-size: 0.9em;
  color: #666;
}

.item-total {
  font-weight: 600;
  color: #28a745;
}

.cart-total {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 2px solid #dee2e6;
  text-align: right;
}

.cart-total h3 {
  margin: 0;
  color: #333;
  font-size: 1.3em;
}

.order-form {
  background: white;
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.order-form h2 {
  margin-bottom: 30px;
  color: #333;
}

.form-section {
  margin-bottom: 30px;
}

.form-section h3 {
  margin-bottom: 20px;
  color: #333;
  font-size: 1.2em;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 15px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: 600;
  color: #333;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 1em;
  transition: border-color 0.3s ease;
}

.form-group input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.payment-methods {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.payment-method {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 15px;
  border: 2px solid #eee;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.payment-method:hover {
  border-color: #007bff;
  background-color: #f8f9fa;
}

.payment-method.selected {
  border-color: #007bff;
  background-color: #e3f2fd;
}

.payment-icon {
  font-size: 1.5em;
  color: #007bff;
  width: 40px;
  text-align: center;
}

.payment-info {
  flex: 1;
}

.payment-info h4 {
  margin: 0 0 5px 0;
  font-size: 1.1em;
}

.payment-info p {
  margin: 0;
  font-size: 0.9em;
  color: #666;
}

.payment-radio {
  display: flex;
  align-items: center;
}

.payment-radio input[type="radio"] {
  display: none;
}

.payment-radio label {
  width: 20px;
  height: 20px;
  border: 2px solid #ddd;
  border-radius: 50%;
  cursor: pointer;
  position: relative;
  transition: all 0.3s ease;
}

.payment-radio input[type="radio"]:checked + label {
  border-color: #007bff;
  background-color: #007bff;
}

.payment-radio input[type="radio"]:checked + label::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 8px;
  height: 8px;
  background-color: white;
  border-radius: 50%;
}

.checkout-actions {
  display: flex;
  gap: 15px;
  margin-top: 30px;
}

.btn-back {
  flex: 1;
  padding: 15px;
  background-color: #6c757d;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: background-color 0.3s ease;
}

.btn-back:hover {
  background-color: #5a6268;
}

.btn-confirm-order {
  flex: 2;
  padding: 15px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: background-color 0.3s ease;
}

.btn-confirm-order:hover:not(:disabled) {
  background-color: #218838;
}

.btn-confirm-order:disabled {
  background-color: #6c757d;
  cursor: not-allowed;
}

/* Modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  width: 90%;
  max-width: 500px;
  max-height: 80vh;
  overflow: hidden;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #eee;
}

.modal-header h3 {
  margin: 0;
  color: #333;
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

.modal-body {
  padding: 20px;
}

.success-message,
.error-message {
  text-align: center;
}

.success-message i {
  font-size: 3em;
  color: #28a745;
  margin-bottom: 15px;
}

.error-message i {
  font-size: 3em;
  color: #dc3545;
  margin-bottom: 15px;
}

.success-message h4,
.error-message h4 {
  margin: 0 0 10px 0;
  color: #333;
}

.success-message p,
.error-message p {
  margin: 0 0 10px 0;
  color: #666;
}

.modal-footer {
  padding: 20px;
  border-top: 1px solid #eee;
  text-align: right;
}

.btn-primary {
  background-color: #007bff;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 1em;
  transition: background-color 0.3s ease;
}

.btn-primary:hover {
  background-color: #0056b3;
}

/* Responsive */
@media (max-width: 768px) {
  .checkout-content {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .form-row {
    grid-template-columns: 1fr;
  }
  
  .checkout-actions {
    flex-direction: column;
  }
  
  .cart-item {
    grid-template-columns: 60px 1fr;
    gap: 10px;
  }
  
  .item-quantity,
  .item-total {
    grid-column: 2;
    justify-self: start;
  }
}
</style> 