<script setup>
import Overlay from '@/components/Overlay.vue';
import Footer from './components/Footer.vue';

import { computed, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';

import DefaultHeader from '@/components/DefaultHeader.vue';
import AuthHeader from '@/components/AuthHeader.vue';

const authStore = useAuthStore();

// 로그인 상태 확인 및 헤더 컴포넌트 선택
const currentHeader = computed(() => {
  return authStore.isLoggedIn ? AuthHeader : DefaultHeader;
});

// 초기화: 페이지 로드 시 세션 상태 확인
onMounted(() => {
  authStore.checkSession(); // 세션 상태 확인
});
</script>

<template>
  <div class="header-container">
    <component :is="currentHeader" />
  </div>

  <Overlay />
  <div class="content-container">
    <RouterView />
  </div>

  <div class="footer-container">
    <Footer />
  </div>
</template>

<style scoped>
.content-container {
  position: relative;
  z-index: 1;
}

.header-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 3;
  padding: 20px 30px;
}

.footer-container {
  position: relative;
  z-index: 2;
}
</style>
