<template>
  <div class="main-content">
    <div class="modal-content">
      <h1>MyPage</h1>
      <div class="my-page">
        <!-- 네비게이션 바 -->
        <nav class="tabs">
          <button v-for="tab in tabs" :key="tab.name" :class="{ active: currentTab === tab.name }"
            @click="currentTab = tab.name">
            {{ tab.label }}
          </button>
        </nav>

        <!-- 탭 콘텐츠 -->
        <section class="tab-content">
          <div v-if="currentTab === 'user-info'" class="user-info">
            <div v-if="user">
              <div class="user-details">
                <p><strong>Name:</strong> {{ user.name }}</p>
                <p><strong>Phone:</strong> {{ user.phone }}</p>
              </div>
            </div>
            <div v-else>
              <p>Loading user information...</p>
            </div>
          </div>

          <div v-else-if="currentTab === 'bookmarked-routes'" class="bookmarked-routes">
            <div v-if="bookmarkedRoutes.length">
              <ul>
                <li v-for="route in bookmarkedRoutes" :key="route.id">
                  {{ route.routeId }}
                </li>
              </ul>
            </div>
            <div v-else>
              <p>No bookmarked routes.</p>
            </div>
          </div>

          <div v-else-if="currentTab === 'completed-routes'" class="completed-routes">
            <div v-if="completedRoutes.length">
              <ul>
                <li v-for="route in completedRoutes" :key="route.id">
                  {{ route.routeId }}
                </li>
              </ul>
            </div>
            <div v-else>
              <p>No completed routes yet.</p>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
import axios from "axios";
import { useAuthStore } from "@/stores/auth";

// 탭 관리
const tabs = [
  { name: "user-info", label: "User Information" },
  { name: "bookmarked-routes", label: "Bookmarked Routes" },
  { name: "completed-routes", label: "Completed Routes" },
];
const currentTab = ref("user-info"); // 기본 탭 설정

// 데이터 상태
const authStore = useAuthStore();

const user = ref(null);
const bookmarkedRoutes = ref([]);
const completedRoutes = ref([]);

const userId = authStore.userId;

// 데이터 가져오기 함수
const fetchUserData = async () => {
  try {
    const userResponse = await axios.get(`/api/user/${userId}`);
    user.value = userResponse.data;

    const bookmarksResponse = await axios.get(`/api/bookmarks/user/${userId}`);
    bookmarkedRoutes.value = bookmarksResponse.data;

    const completedResponse = await axios.get(`/api/completed-routes/user/${userId}`);
    completedRoutes.value = completedResponse.data;
  } catch (error) {
    console.error("Error fetching data:", error);
  }
};

// 컴포넌트 마운트 시 데이터 가져오기
onMounted(fetchUserData);
</script>


<style scoped>
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
  max-width: 600px;
  min-height: 400px;
  max-height: 480px;
  padding: 20px;
  position: relative;
  text-align: center;
}

.my-page {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

h1 {
  text-align: center;
}

.tabs {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.tabs button {
  background: #ddd;
  border: none;
  padding: 10px 20px;
  margin: 0 5px;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
}

.tabs button.active {
  background: #6a994e;
  color: white;
}

.tab-content {
  padding: 20px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0px 4px 8px rgba(0, 0, 0, 0.1);
  background: rgba(255, 255, 255, 0.3);
}

ul {
  list-style: none;
  padding: 0;
}

ul li {
  margin-bottom: 10px;
}

.user-details p {
  margin: 5px 0;
  font-size: 16px;
}

.user-details p strong {
  font-weight: bold;
}
</style>