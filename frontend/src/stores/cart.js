import { defineStore } from 'pinia'
import { EcommerceApi } from '@/services/http-common'
import authService from '@/services/authService'
import axios from 'axios'

const API_URL = 'http://localhost:8080/api'

// Fonction temporaire pour debug
function getAuthHeader() {
  return {
    Authorization: `Bearer ${sessionStorage.getItem('accessToken')}`
  }
}

export const useCartStore = defineStore('cart', {
  state: () => ({
    items: [],
    isCartOpen: false
  }),

  getters: {
    // Nombre total d'articles dans le panier
    totalItems: (state) => {
      return state.items.reduce((total, item) => total + item.quantity, 0)
    },

    // Prix total du panier
    totalPrice: (state) => {
      return state.items.reduce((total, item) => {
        return total + (item.price * item.quantity)
      }, 0)
    },

    // Vérifier si le panier est vide
    isEmpty: (state) => {
      return state.items.length === 0
    },

    // Formater le prix total
    formattedTotalPrice: (state) => {
      return new Intl.NumberFormat('fr-FR', {
        style: 'currency',
        currency: 'EUR'
      }).format(state.items.reduce((total, item) => {
        return total + (item.price * item.quantity)
      }, 0))
    }
  },

  actions: {
    // Initialiser les écouteurs d'événements
    initEventListeners() {
      // Écouter la connexion d'un utilisateur
      window.addEventListener('user-authenticated', () => {
        console.log('Utilisateur connecté, rechargement du panier...')
        this.loadCart()
      })
      
      // Écouter la déconnexion d'un utilisateur
      window.addEventListener('user-logged-out', () => {
        console.log('Utilisateur déconnecté, vidage du panier...')
        this.items = []
        this.isCartOpen = false
      })
    },

    async loadCart() {
      console.log('=== loadCart() appelé ===')
      console.log('Utilisateur authentifié:', authService.isAuthenticated())
      console.log('Token présent:', !!sessionStorage.getItem('accessToken'))
      console.log('Token complet:', sessionStorage.getItem('accessToken'))
      
      if (authService.isAuthenticated()) {
        const userId = authService.getCurrentUser().id
        console.log('ID utilisateur:', userId)
        console.log('URL appelée:', `${API_URL}/cart/user/${userId}`)
        
        try {
          console.log('=== AVANT L\'APPEL API ===')
          console.log('URL complète:', `${API_URL}/cart/user/${userId}`)
          console.log('Headers de la requête:', EcommerceApi.defaults.headers)
          console.log('Token dans sessionStorage:', sessionStorage.getItem('accessToken'))
          console.log('Token envoyé dans les headers :', getAuthHeader())
          console.log('Token complet (pour debug):', sessionStorage.getItem('accessToken'))
          
          // Test avec axios direct pour comparer
          console.log('=== TEST AVEC AXIOS DIRECT ===')
          try {
            const testResponse = await axios.get(`${API_URL}/cart/user/${userId}`, {
              headers: getAuthHeader()
            })
            console.log('Test axios direct réussi:', testResponse.data)
          } catch (testError) {
            console.error('Test axios direct échoué:', testError.response?.data)
          }
          
          const response = await EcommerceApi.get(`/cart/user/${userId}`)
          console.log('Réponse API panier utilisateur connecté :', response.data)

          const lignes = (response.data && response.data.lignes) ? response.data.lignes : []
          console.log('Lignes extraites:', lignes)
          
          this.items = lignes.map(ligne => ({
            id: ligne.produit.id,
            name: ligne.produit.nom,
            price: ligne.prixUnitaire,
            quantity: ligne.quantite,
            image: ligne.produit.thumbnailUrl,
          }))
          console.log('Items mappés:', this.items)
        } catch (error) {
          console.error('Erreur lors du chargement du panier:', error)
          console.error('Détails de l\'erreur:', error.response?.data)
        }
      } else {
        console.log('Utilisateur non connecté, chargement depuis localStorage')
        this.loadFromLocalStorage()
      }
    },

    // Ajouter un produit au panier
    async addToCart(product) {
      console.log('=== addToCart() appelé ===')
      console.log('Produit à ajouter:', product)
      console.log('Utilisateur authentifié:', authService.isAuthenticated())
      
      if (authService.isAuthenticated()) {
        // Vérifier que l'utilisateur est un CLIENT, pas un GESTIONNAIRE
        if (authService.isGestionnaire()) {
          console.warn('Les gestionnaires ne peuvent pas ajouter de produits au panier')
          throw new Error('Les gestionnaires ne peuvent pas ajouter de produits au panier')
        }
        
        const userId = authService.getCurrentUser().id
        console.log('ID utilisateur:', userId)
        
        try {
          const response = await EcommerceApi.post(`/cart/add`, null, {
            params: { userId, productId: product.id, quantite: 1 }
          })
          console.log('Réponse API ajout au panier:', response.data)
          
          // Recharger le panier après l'ajout
          await this.loadCart()
          console.log('Panier rechargé après ajout')
        } catch (error) {
          console.error('Erreur lors de l\'ajout au panier:', error)
          throw error
        }
      } else {
        console.log('Utilisateur non connecté, ajout au localStorage')
        const existingItem = this.items.find(item => item.id === product.id)
        if (existingItem) {
          existingItem.quantity += 1
        } else {
          this.items.push({
            id: product.id,
            name: product.nom,
            price: product.prix,
            quantity: 1,
            image: product.thumbnailUrl || 'https://via.placeholder.com/100x100?text=No+Image',
            description: product.description,
            brand: product.marque
          })
        }
        this.saveToLocalStorage()
      }
    },

    // Retirer un produit du panier
    async removeFromCart(productId) {
      if (authService.isAuthenticated()) {
        const userId = authService.getCurrentUser().id
        await EcommerceApi.delete(`/cart/remove`, {
          params: { userId, productId }
        })
        await this.loadCart()
      } else {
        const index = this.items.findIndex(item => item.id === productId)
        if (index > -1) {
          this.items.splice(index, 1)
          this.saveToLocalStorage()
        }
      }
    },

    // Modifier la quantité d'un produit
    async updateQuantity(productId, quantity) {
      if (authService.isAuthenticated()) {
        const userId = authService.getCurrentUser().id
        await EcommerceApi.put(`/cart/update`, null, {
          params: { userId, productId, quantite: quantity }
        })
        await this.loadCart()
      } else {
        const item = this.items.find(item => item.id === productId)
        if (item) {
          if (quantity <= 0) {
            this.removeFromCart(productId)
          } else {
            item.quantity = quantity
            this.saveToLocalStorage()
          }
        }
      }
    },

    // Vider le panier
    async clearCart() {
      if (authService.isAuthenticated()) {
        const userId = authService.getCurrentUser().id
        await EcommerceApi.delete(`/cart/clear`, {
          params: { userId }
        })
        await this.loadCart()
      } else {
        this.items = []
        this.saveToLocalStorage()
      }
    },

    // Ouvrir/fermer le panier
    toggleCart() {
      this.isCartOpen = !this.isCartOpen
    },

    // Ouvrir le panier
    openCart() {
      this.isCartOpen = true
    },

    // Fermer le panier
    closeCart() {
      this.isCartOpen = false
    },

    // Sauvegarder dans le localStorage
    saveToLocalStorage() {
      localStorage.setItem('cart', JSON.stringify(this.items))
    },

    // Charger depuis le localStorage
    loadFromLocalStorage() {
      const savedCart = localStorage.getItem('cart')
      if (savedCart) {
        this.items = JSON.parse(savedCart)
      }
    }
  }
}) 