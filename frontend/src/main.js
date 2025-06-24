//import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { useCartStore } from './stores/cart'

import App from './App.vue'
import router from './router'

// Import Bootstrap CSS and JS
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// Initialiser les écouteurs d'événements du store panier
const cartStore = useCartStore(pinia)
cartStore.initEventListeners()

app.mount('#app')
