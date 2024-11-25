import './assets/css/style.css'

import { library } from '@fortawesome/fontawesome-svg-core';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { faStar as fasStar } from '@fortawesome/free-solid-svg-icons';
import { faStar as farStar } from '@fortawesome/free-regular-svg-icons';

// 라이브러리에 아이콘 추가
library.add(fasStar, farStar);

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

const app = createApp(App)

// 전역 컴포넌트 등록
app.component('font-awesome-icon', FontAwesomeIcon);

import { useKakao } from 'vue3-kakao-maps/@utils';

useKakao('d8b2f23728c836fc183a3d2b7f5fa724');

app.use(createPinia())
app.use(router)

app.mount('#app')
