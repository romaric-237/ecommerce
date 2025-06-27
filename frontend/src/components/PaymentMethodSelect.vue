<template>
  <div class="payment-method-select">
    <div class="form-check mb-2" v-for="method in methods" :key="method.value">
      <input class="form-check-input" type="radio" :id="method.value" :value="method.value" v-model="modelValue">
      <label class="form-check-label" :for="method.value">
        <i :class="method.icon" class="me-2"></i>
        {{ method.label }}
      </label>
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';
const props = defineProps({
  modelValue: String
});
const emit = defineEmits(['update:modelValue']);
const methods = [
  { value: 'CREDIT_CARD', label: 'Carte de crédit', icon: 'fas fa-credit-card' },
  { value: 'DEBIT_CARD', label: 'Carte de débit', icon: 'fas fa-credit-card' },
  { value: 'BANK_TRANSFER', label: 'Virement bancaire', icon: 'fas fa-university' },
  { value: 'PAYPAL', label: 'PayPal', icon: 'fab fa-paypal' },
  { value: 'PAYSAFE_CARD', label: 'Paysafe card', icon: 'fas fa-ticket-alt' }
];
</script>

<script>
export default {
  name: 'PaymentMethodSelect',
  props: ['modelValue'],
  emits: ['update:modelValue'],
  methods: {
    updateValue(value) {
      this.$emit('update:modelValue', value);
    }
  }
};
</script>

<style scoped>
.payment-method-select {
  display: flex;
  flex-direction: column;
  gap: 8px;
}
.form-check-input:checked {
  border-color: #42b983;
  background-color: #42b983;
}
</style> 