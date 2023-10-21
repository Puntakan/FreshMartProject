import { createRouter, createWebHistory } from 'vue-router'
import HistoryRouter from '../component/History.vue'
import CashierRouter from '../component/Cashier.vue'

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            name: 'CashierPage',
            component: CashierRouter
        },
        {
            path: '/history',
            name: 'HistoryPage',
            component: HistoryRouter
        }
    ]
})
export default router