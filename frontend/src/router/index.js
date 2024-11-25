import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from "@/stores/auth";
import HomeContent from '@/views/HomeContent.vue';
import Login from '@/components/Login.vue';
import Signup from '@/components/Signup.vue';
import MainContent from '@/views/MainContent.vue';
import RouteForm from '@/views/RouteForm.vue';
import UserProfile from '@/views/MypageContent.vue';
import FormResult from '@/components/FormResult.vue';
import FileUpload from '@/views/FileUpload.vue';
import ReviewForm from '@/views/ReviewForm.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: HomeContent,
      component: HomeContent,
    },
    {
      path: '/login',
      name: Login,
      component: Login,
    },
    {
      path: '/signup',
      name: Signup,
      component: Signup,
    },
    {
      path: '/home',
      name: MainContent,
      component: MainContent,
      meta: { requiresAuth: true }
    },
    {
      path: '/form',
      name: RouteForm,
      component: RouteForm,
      meta: { requiresAuth: true }
    },
    {
      path: '/mypage/:id',
      name: UserProfile,
      component: UserProfile,
      meta: { requiresAuth: true }
    },
    {
      path: '/form/result',
      name: FormResult,
      component: FormResult,
      meta: { requiresAuth: true }
    },
    {
      path: '/upload',
      name: FileUpload,
      component: FileUpload,
      meta: { requiresAuth: true }
    },
    {
      path: '/review',
      name: ReviewForm,
      component: ReviewForm,
      meta: { requiresAuth: true }
    },
  ],
})

router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore();

  // 세션 상태 확인
  await authStore.checkSession();

  // 로그인한 상태라면 '/' 또는 '/login', '/signup' 접근 차단
  if (authStore.isLoggedIn && (to.path === '/' || to.path === '/login' || to.path === '/signup')) {
    return next('/home'); // 로그인 후 홈으로 리다이렉트
  }

  // 인증이 필요한 경로에 대한 처리
  if (to.meta.requiresAuth) {
    if (authStore.isLoggedIn) {
      next();
    } else {
      next("/login"); // 로그인되지 않은 사용자는 로그인 페이지로
    }
  } else {
    next(); // 인증이 필요 없는 경로는 그대로 진행
  }
});



export default router
