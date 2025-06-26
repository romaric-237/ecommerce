<template>
  <form @submit.prevent="handleSubmit" class="product-edit-form">
    <!-- Nom du produit -->
    <div class="mb-3">
      <label for="nom" class="form-label">Nom du produit *</label>
      <input
        type="text"
        class="form-control"
        id="nom"
        v-model="formData.nom"
        :class="{ 'is-invalid': errors.nom }"
        required
        minlength="2"
        maxlength="100"
      >
      <div class="invalid-feedback" v-if="errors.nom">
        {{ errors.nom }}
      </div>
    </div>

    <!-- Description -->
    <div class="mb-3">
      <label for="description" class="form-label">Description *</label>
      <textarea
        class="form-control"
        id="description"
        v-model="formData.description"
        :class="{ 'is-invalid': errors.description }"
        rows="3"
        required
        minlength="10"
        maxlength="1000"
      ></textarea>
      <div class="invalid-feedback" v-if="errors.description">
        {{ errors.description }}
      </div>
    </div>

    <!-- Marque -->
    <div class="mb-3">
      <label for="marque" class="form-label">Marque *</label>
      <input
        type="text"
        class="form-control"
        id="marque"
        v-model="formData.marque"
        :class="{ 'is-invalid': errors.marque }"
        required
        minlength="2"
        maxlength="50"
      >
      <div class="invalid-feedback" v-if="errors.marque">
        {{ errors.marque }}
      </div>
    </div>

    <!-- Prix -->
    <div class="mb-3">
      <label for="prix" class="form-label">Prix (€) *</label>
      <input
        type="number"
        class="form-control"
        id="prix"
        v-model.number="formData.prix"
        :class="{ 'is-invalid': errors.prix }"
        required
        min="0.01"
        max="999999.99"
        step="0.01"
      >
      <div class="invalid-feedback" v-if="errors.prix">
        {{ errors.prix }}
      </div>
    </div>

    <!-- URL de l'image -->
    <div class="mb-3">
      <label for="thumbnailUrl" class="form-label">URL de l'image *</label>
      <input
        type="url"
        class="form-control"
        id="thumbnailUrl"
        v-model="formData.thumbnailUrl"
        :class="{ 'is-invalid': errors.thumbnailUrl }"
        required
        pattern="https?://.*"
      >
      <div class="invalid-feedback" v-if="errors.thumbnailUrl">
        {{ errors.thumbnailUrl }}
      </div>
      <div class="form-text">
        L'URL doit commencer par http:// ou https://
      </div>
    </div>

    <!-- Genre -->
    <div class="mb-3">
      <label for="genre" class="form-label">Genre *</label>
      <select
        class="form-select"
        id="genre"
        v-model="formData.genre"
        :class="{ 'is-invalid': errors.genre }"
        required
      >
        <option value="">Sélectionner un genre</option>
        <option value="HOMME">Homme</option>
        <option value="FEMME">Femme</option>
        <option value="UNISEXE">Unisexe</option>
      </select>
      <div class="invalid-feedback" v-if="errors.genre">
        {{ errors.genre }}
      </div>
    </div>

    <!-- Taille -->
    <div class="mb-3">
      <label for="taille" class="form-label">Taille *</label>
      <select
        class="form-select"
        id="taille"
        v-model="formData.taille"
        :class="{ 'is-invalid': errors.taille }"
        required
      >
        <option value="">Sélectionner une taille</option>
        <option value="XS">XS</option>
        <option value="S">S</option>
        <option value="M">M</option>
        <option value="L">L</option>
        <option value="XL">XL</option>
        <option value="XXL">XXL</option>
      </select>
      <div class="invalid-feedback" v-if="errors.taille">
        {{ errors.taille }}
      </div>
    </div>

    <!-- Catégorie -->
    <div class="mb-3">
      <label for="categoryId" class="form-label">Catégorie *</label>
      <select
        class="form-select"
        id="categoryId"
        v-model.number="formData.categoryId"
        :class="{ 'is-invalid': errors.categoryId }"
        required
      >
        <option value="">Sélectionner une catégorie</option>
        <option
          v-for="category in categories"
          :key="category.id"
          :value="category.id"
        >
          {{ category.nom }}
        </option>
      </select>
      <div class="invalid-feedback" v-if="errors.categoryId">
        {{ errors.categoryId }}
      </div>
    </div>

    <!-- Aperçu de l'image -->
    <div class="mb-3" v-if="formData.thumbnailUrl">
      <label class="form-label">Aperçu de l'image</label>
      <div class="image-preview">
        <img
          :src="formData.thumbnailUrl"
          :alt="formData.nom"
          class="img-thumbnail"
          @error="handleImageError"
        >
      </div>
    </div>

    <!-- Boutons d'action -->
    <div class="d-flex gap-2">
      <button
        type="submit"
        class="btn btn-primary"
        :disabled="loading || !isFormValid"
      >
        <span v-if="loading" class="spinner-border spinner-border-sm me-2"></span>
        <i v-else class="fas fa-save me-2"></i>
        {{ loading ? 'Enregistrement...' : 'Enregistrer' }}
      </button>
      
      <button
        type="button"
        class="btn btn-secondary"
        @click="$emit('cancel')"
        :disabled="loading"
      >
        <i class="fas fa-times me-2"></i>
        Annuler
      </button>
    </div>
  </form>
</template>

<script>
export default {
  name: 'ProductEditForm',
  props: {
    product: {
      type: Object,
      required: true
    },
    categories: {
      type: Array,
      default: () => []
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      formData: {
        nom: '',
        description: '',
        marque: '',
        prix: 0,
        thumbnailUrl: '',
        genre: 'HOMME',
        taille: 'M',
        categoryId: null
      },
      errors: {}
    };
  },
  computed: {
    isFormValid() {
      return this.formData.nom &&
             this.formData.description &&
             this.formData.marque &&
             this.formData.prix > 0 &&
             this.formData.thumbnailUrl &&
             this.formData.genre &&
             this.formData.taille &&
             this.formData.categoryId;
    }
  },
  watch: {
    product: {
      immediate: true,
      handler(newProduct) {
        if (newProduct) {
          this.formData = {
            nom: newProduct.nom || '',
            description: newProduct.description || '',
            marque: newProduct.marque || '',
            prix: newProduct.prix || 0,
            thumbnailUrl: newProduct.thumbnailUrl || '',
            genre: newProduct.genre || 'HOMME',
            taille: newProduct.taille || 'M',
            categoryId: newProduct.category?.id || newProduct.categoryId || null
          };
        }
      }
    }
  },
  methods: {
    validateForm() {
      this.errors = {};

      // Validation du nom
      if (!this.formData.nom) {
        this.errors.nom = 'Le nom du produit est obligatoire';
      } else if (this.formData.nom.length < 2) {
        this.errors.nom = 'Le nom doit contenir au moins 2 caractères';
      } else if (this.formData.nom.length > 100) {
        this.errors.nom = 'Le nom ne peut pas dépasser 100 caractères';
      }

      // Validation de la description
      if (!this.formData.description) {
        this.errors.description = 'La description est obligatoire';
      } else if (this.formData.description.length < 10) {
        this.errors.description = 'La description doit contenir au moins 10 caractères';
      } else if (this.formData.description.length > 1000) {
        this.errors.description = 'La description ne peut pas dépasser 1000 caractères';
      }

      // Validation de la marque
      if (!this.formData.marque) {
        this.errors.marque = 'La marque est obligatoire';
      } else if (this.formData.marque.length < 2) {
        this.errors.marque = 'La marque doit contenir au moins 2 caractères';
      } else if (this.formData.marque.length > 50) {
        this.errors.marque = 'La marque ne peut pas dépasser 50 caractères';
      }

      // Validation du prix
      if (!this.formData.prix || this.formData.prix <= 0) {
        this.errors.prix = 'Le prix doit être supérieur à 0';
      } else if (this.formData.prix > 999999.99) {
        this.errors.prix = 'Le prix ne peut pas dépasser 999999.99';
      }

      // Validation de l'URL
      if (!this.formData.thumbnailUrl) {
        this.errors.thumbnailUrl = 'L\'URL de l\'image est obligatoire';
      } else if (!this.formData.thumbnailUrl.match(/^https?:\/\/.*/)) {
        this.errors.thumbnailUrl = 'L\'URL doit commencer par http:// ou https://';
      }

      // Validation du genre
      if (!this.formData.genre) {
        this.errors.genre = 'Le genre est obligatoire';
      }

      // Validation de la taille
      if (!this.formData.taille) {
        this.errors.taille = 'La taille est obligatoire';
      }

      // Validation de la catégorie
      if (!this.formData.categoryId) {
        this.errors.categoryId = 'La catégorie est obligatoire';
      }

      return Object.keys(this.errors).length === 0;
    },

    handleSubmit() {
      if (this.validateForm()) {
        this.$emit('submit', { ...this.formData });
      }
    },

    handleImageError(event) {
      event.target.src = '/placeholder-image.jpg';
    }
  }
};
</script>

<style scoped>
.product-edit-form {
  max-width: 100%;
}

.image-preview {
  max-width: 200px;
}

.image-preview img {
  max-width: 100%;
  height: auto;
}

.form-label {
  font-weight: 600;
  color: #495057;
}

.form-control:focus,
.form-select:focus {
  border-color: #80bdff;
  box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
}

.invalid-feedback {
  display: block;
}

.form-text {
  font-size: 0.875rem;
  color: #6c757d;
}
</style> 