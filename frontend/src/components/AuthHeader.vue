<template>
  <header class="header">
    <!-- Logo Section -->
    <div class="logo-container">
      <img src="@/assets/logo/team_logo.png" alt="Logo" class="logo-image" />
      <span class="app-name">URS</span>
    </div>

    <!-- Navigation Links -->
    <nav class="nav-links">
      <RouterLink to="/home" class="nav-link">Home</RouterLink>
      <RouterLink to="/routes" class="nav-link">Route</RouterLink>
      <RouterLink to="/mypage" class="nav-link">MyPage</RouterLink>
    </nav>

    <div>
      <span id="show-username">Welcome, {{ authStore.username }}</span>
      <!-- Logout Button -->
      <button class="logout-button" @click="logout" :disabled="isLoading">
        <span v-if="isLoading">Logging out...</span>
        <span v-else>Log Out</span>
      </button>
    </div>
  </header>
</template>

<script setup>
import { ref } from 'vue';
const isLoading = ref(false);
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();

const logout = async () => {
  isLoading.value = true;

  try {
    await authStore.logout();
    router.push("/");
  } catch (error) {
    alert("Failed to log out. Please try again."); // 로그아웃 실패 메시지
  } finally {
    isLoading.value = false; // Reset loading state
  }
};
</script>

<style scoped>
#show-username {
  margin-right: 12px;
  color: #fff;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 20px;
  background-color: #343434;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo-image {
  width: 60px;
  height: 60px;
  margin-right: 10px;
}

.app-name {
  font-size: 40px;
  font-weight: bold;
  color: #fff;
}

.nav-links {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav-link {
  text-decoration: none;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  transition: color 0.3s ease;
  margin-left: 20px;
  margin-right: 20px;
}

.nav-link:hover {
  color: #ffb703;
}

.logout-button {
  background-color: transparent;
  color: #fff;
  border: 2px solid #ffb703;
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease, color 0.3s ease;
}

.logout-button:hover {
  background-color: #ffb703;
  color: #fff;
}
</style>
