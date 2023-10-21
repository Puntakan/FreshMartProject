<script setup>
import { RouterLink } from 'vue-router'
</script>
 
<template>
    <div>
        <RouterLink :to="{ name: 'CashierPage' }">Cashier</RouterLink>
        <RouterLink :to="{ name: 'HistoryPage' }">History</RouterLink>
    </div>
</template>
 
<style scoped>
</style>