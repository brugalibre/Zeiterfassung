import { createApp } from 'vue'
import App from './App.vue'
import VueChartkick from 'vue-chartkick'
import 'chartkick/chart.js'

const app = createApp(App);
app.use(VueChartkick);
app.mount('#app');
