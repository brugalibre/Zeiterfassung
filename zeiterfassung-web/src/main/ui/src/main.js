import {createApp} from 'vue'
import App from './App.vue'
import VueChartkick from 'vue-chartkick'
import 'chartkick/chart.js'
import {store} from './store/store';

const app = createApp(App);
app.use(VueChartkick);
app.use(store);
app.mount('#app');
