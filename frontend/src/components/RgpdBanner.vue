<template>
  <div v-if="!hasAcceptedCookies" class="rgpd-banner">
    <div class="rgpd-content">
      <p>
        Nous utilisons des cookies pour améliorer votre expérience sur notre site.
        En continuant à naviguer, vous acceptez notre utilisation des cookies.
        Pour plus d'informations, consultez notre 
        <a href="#" @click.prevent="showPrivacyPolicy">politique de confidentialité</a>.
      </p>
      <div class="rgpd-buttons">
        <button @click="acceptCookies" class="btn-accept">Accepter</button>
        <button @click="rejectCookies" class="btn-reject">Refuser</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'RgpdBanner',
  data() {
    return {
      hasAcceptedCookies: false
    }
  },
  created() {
    // Vérifier si l'utilisateur a déjà fait un choix
    const cookieChoice = localStorage.getItem('cookieChoice')
    if (cookieChoice) {
      this.hasAcceptedCookies = true
    }
  },
  methods: {
    acceptCookies() {
      localStorage.setItem('cookieChoice', 'accepted')
      this.hasAcceptedCookies = true
      this.$emit('cookies-accepted')
    },
    rejectCookies() {
      localStorage.setItem('cookieChoice', 'rejected')
      this.hasAcceptedCookies = true
      this.$emit('cookies-rejected')
    },
    showPrivacyPolicy() {
      // Émettre un événement pour afficher la politique de confidentialité
      this.$emit('show-privacy-policy')
    }
  }
}
</script>

<style scoped>
.rgpd-banner {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  background-color: rgba(0, 0, 0, 0.9);
  color: white;
  padding: 1rem;
  z-index: 1000;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
}

.rgpd-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 1rem;
}

.rgpd-content p {
  margin: 0;
  font-size: 0.9rem;
  line-height: 1.4;
}

.rgpd-content a {
  color: #4f46e5;
  text-decoration: underline;
}

.rgpd-buttons {
  display: flex;
  gap: 0.5rem;
}

.btn-accept, .btn-reject {
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: 500;
  transition: all 0.3s ease;
}

.btn-accept {
  background-color: #4f46e5;
  color: white;
}

.btn-accept:hover {
  background-color: #4338ca;
}

.btn-reject {
  background-color: #e5e7eb;
  color: #374151;
}

.btn-reject:hover {
  background-color: #d1d5db;
}

@media (max-width: 768px) {
  .rgpd-content {
    flex-direction: column;
    text-align: center;
  }

  .rgpd-buttons {
    justify-content: center;
  }
}
</style> 