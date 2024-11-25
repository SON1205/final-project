<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth"; // Pinia store 사용
import { useRouter } from "vue-router";

const router = useRouter();
const authStore = useAuthStore();

const userId = ref("");
const password = ref("");
const isLoading = ref(false); // 로그인 진행 상태
const errorMessage = ref(""); // 에러 메시지 상태

const handleLogin = async () => {
  isLoading.value = true; // 로딩 상태 활성화
  errorMessage.value = ""; // 이전 에러 메시지 초기화

  try {
    await authStore.login({ userId: userId.value, password: password.value });
    router.push("/home"); // 로그인 성공 시 보호된 페이지로 이동
  } catch (error) {
    // 로그인 실패 시 에러 메시지 표시
    errorMessage.value = error.message || "Login failed. Please check your ID and password.";
  } finally {
    isLoading.value = false; // 로딩 상태 비활성화
  }
};
</script>

<template>
  <div class="main-content">
    <div class="modal-content">
      <!-- 닫기 버튼 -->
      <button class="close-btn" @click="$router.push('/')">×</button>

      <!-- 로그인 폼 -->
      <form class="form signup" @submit.prevent="handleLogin">
        <div class="form-header">Login</div>

        <!-- 사용자 ID -->
        <div class="form-elements">
          <label>
            <input type="text" v-model="userId" placeholder="👤 Type your ID" aria-label="User ID" required />
          </label>

          <!-- 비밀번호 -->
          <label>
            <input type="password" v-model="password" placeholder="🔑 Type your Password" aria-label="Password"
              required />
          </label>

          <!-- 비밀번호 찾기 링크 -->
          <div class="form-element forgot-password">
            <router-link to="/forgot-password">Forgot password?</router-link>
          </div>

          <!-- 에러 메시지 -->
          <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>

          <!-- 로그인 버튼 -->
          <button id="submit-btn" type="submit" :disabled="isLoading">
            <div v-if="isLoading" class="loading-overlay">
              <div class="loading-spinner"></div>
            </div>
            <span v-else>LOGIN</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>


<style scoped>
.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  z-index: 9999;
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-spinner {
  width: 50px;
  height: 50px;
  border: 5px solid rgba(255, 255, 255, 0.2);
  border-top: 5px solid #ffb703;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}


.main-content {
  position: relative;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background: rgb(22, 22, 22);
  border: 0.0625rem solid rgba(128, 128, 128, 0.4);
  border-radius: 0.5rem;
  box-shadow: rgba(0, 0, 0, 0.8) 0px 0.25rem 0.5rem 0px;
  color: rgba(255, 255, 255, 0.7);
  width: 90%;
  max-width: 500px;
  padding: 20px;
  position: relative;
  text-align: center;
}

.close-btn {
  position: absolute;
  top: 15px;
  right: 15px;
  background: transparent;
  border: none;
  font-size: 25px;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
}

.form {
  text-align: center;
}

.form .form-header {
  font-size: 45px;
  font-weight: bold;
  margin-bottom: 50px;
}

.form .form-elements {
  margin-top: 10px;
}

.form .form-elements input {
  width: 100%;
  padding: 10px;
  font-size: 16px;
  margin: 10px 0;
  border-radius: 8px;
  border: 1px solid #444;
  background: #f9f9f9;
  box-sizing: border-box;
}

.form .form-elements input:focus {
  outline: none;
  border-color: #ffb703;
  background: #fff;
}

.form .form-elements button {
  width: 100%;
  padding: 12px;
  font-size: 16px;
  font-weight: bold;
  background: #ffb703;
  color: #fff;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s ease, opacity 0.3s ease;
}

.form .form-elements button:hover {
  background: #6a994e;
}

.form .form-elements button:disabled {
  background: #aaa;
  cursor: not-allowed;
  opacity: 0.8;
}

.forgot-password {
  text-align: center;
  margin: 10px 0;
  font-size: 14px;
  color: #aaa;
  cursor: pointer;
}

.forgot-password:hover {
  text-decoration: underline;
}

.error-message {
  color: #ff4d4d;
  font-size: 14px;
  margin-top: 10px;
}
</style>
