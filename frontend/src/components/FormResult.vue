<template>
  <div class="main-content">
    <div class="modal-content">
      <div class="content">
        <div class="map-container">
          <KakaoMap :level=5 :width="700" :height="450" :lat="centerLatitude" :lng="centerLongitude"
            @onLoadKakaoMap="onLoadKakaoMap">
            <!-- 경로에 따른 마커 표시 -->
            <KakaoMapMarker v-for="(point, index) in routePoints" :key="index" :lat="point.latitude"
              :lng="point.longitude" :text="point.label" />
          </KakaoMap>
        </div>
        <div class="info-container">
          <button class="bookmark-btn" @click="toggleBookmark">
            <font-awesome-icon :icon="isBookmarked ? 'fas fa-star' : 'far fa-star'" />
          </button>
          <h2>Selected Route</h2>
          <div class="info">
            <p v-if="route">{{ route.description }}</p>
            <img :src="route?.imageUrl" alt="Route Image" v-if="route?.imageUrl" />
            <div>
              <p>District: {{ route?.districtName }}</p>
              <p>Average Rating: {{ route?.averageRating }}</p>
              <p>Duration: {{ route?.durationTime }} mins</p>
            </div>
          </div>
          <div class="button-group">
            <button @click="navigateToUpload">Authentication</button>
            <button @click="navigateToForm">Search Others</button>
            <button @click="openKakaoMap">View Map Paths</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { KakaoMap, KakaoMapMarker } from "vue3-kakao-maps";
import { ref, onMounted } from "vue";
import axios from "axios";
import { useFormStore } from "../stores/form";
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";

// Stores
const formStore = useFormStore();
const authStore = useAuthStore();
const router = useRouter();

// Data
const centerLatitude = ref(0);
const centerLongitude = ref(0);
const route = ref(null); // 현재 경로 정보
const routePoints = ref([]); // 마커 표시를 위한 좌표 배열
const isBookmarked = ref(false);
const routeId = ref(null);
const userId = authStore.userId;

// API 호출 함수
const fetchRouteInfo = async () => {
  try {
    const response = await axios.get("/api/routes/search", {
      params: {
        district: formStore.selectedDistrict,
        theme: formStore.selectedStep1,
        duration: formStore.selectedStep2,
      },
    });
    route.value = response.data; // 경로 정보 저장
    routeId.value = route.value.routeId; // Route ID 동적 설정

    // 로케이션 및 포인트 정보 가져오기
    const locationResponse = await axios.get(`/api/locations/route/${routeId.value}`);
    const locations = locationResponse.data;

    // 각 포인트의 세부 정보 가져오기
    const points = await Promise.all(
      locations.map(async (location) => {
        const pointResponse = await axios.get(`/api/points/${location.pointId}`);
        const pointData = {
          latitude: pointResponse.data.latitude,
          longitude: pointResponse.data.longitude,
          label: pointResponse.data.name,
        };

        // 'MIDDLE' 타입일 경우 추가 처리
        if (location.type === 'MIDDLE') {
          centerLatitude.value = pointData.latitude;
          centerLongitude.value = pointData.longitude;
        }

        return pointData;
      })
    );

    routePoints.value = points; // 마커 데이터 설정
  } catch (error) {
    console.error("Failed to fetch route info:", error.message);
  }
};

// 북마크 토글
const toggleBookmark = async () => {
  if (!routeId.value) return;
  try {
    if (isBookmarked.value) {
      await axios.delete(`/api/bookmarks/${userId}/${routeId.value}`);
    } else {
      const bookmarkDto = { userId, routeId: routeId.value, createdAt: new Date().toISOString() };
      await axios.post("/api/bookmarks", bookmarkDto);
    }
    isBookmarked.value = !isBookmarked.value; // 상태 토글
  } catch (error) {
    console.error("Bookmark action failed:", error.message);
  }
};

const navigateToUpload = () => {
  router.push("/upload");
};

const navigateToForm = () => {
  router.push("/form");
};

const onLoadKakaoMap = () => {
  console.log("카카오 지도가 로드되었습니다.");
};

// 카카오맵 열기
const openKakaoMap = () => {
  const coordinates = routePoints.value
    .map((point) =>
      `${Math.round(new kakao.maps.LatLng(point.latitude, point.longitude).toCoords().getX())}%2C${Math.round(new kakao.maps.LatLng(point.latitude, point.longitude).toCoords().getY())}`)
    .join("%2C");

  const kakaoMapUrl = `https://map.kakao.com/?map_type=TYPE_MAP&target=walk&rt=${coordinates}`;
  window.open(kakaoMapUrl, "_blank");
};

// 초기화
onMounted(fetchRouteInfo);
</script>

<style scoped>
.info {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.info div {
  display: flex;
  flex-direction: column;
  justify-content: center;
}

img {
  max-width: 30%;
  /* 이미지가 부모 컨테이너 크기를 넘지 않도록 설정 */
  height: auto;
  /* 비율을 유지하며 크기 조정 */
  border-radius: 8px;
  /* 둥근 모서리 추가 (선택 사항) */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  /* 그림자 효과 추가 (선택 사항) */
  margin: 10px;
  margin-right: 20px;
}

.bookmark-btn {
  position: absolute;
  top: 1.8em;
  right: 8.5em;
  background: transparent;
  border: none;
  font-size: 20px;
  cursor: pointer;
  color: #ffb703;
  transition: color 0.3s ease;
}

.bookmark-btn:hover {
  color: #f48c06;
}

.content {
  display: flex;
  justify-content: space-between;
}

.main-content {
  position: relative;
  width: 100vw;
  height: 104vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  width: 100%;
  max-width: 1500px;
  min-height: 500px;
  padding: 20px;
  position: relative;
  text-align: center;
  display: flex;
  justify-content: center;
}

.map-container {
  flex: 2;
  background: rgba(33, 33, 33, 0.95);
  color: rgba(255, 255, 255, 0.85);
  padding: 20px;
  text-align: center;
  margin-right: 20px;
  border: 2px solid white;
  /* 하얀색 테두리 추가 */
  border-radius: 12px;
  /* 모서리를 둥글게 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  /* 약간의 그림자 추가 */
  max-width: 750px;
}

.info-container {
  flex: 1;
  background: rgba(33, 33, 33, 0.95);
  color: rgba(255, 255, 255, 0.85);
  padding: 20px;
  text-align: center;
  margin-left: 20px;
  border: 2px solid white;
  /* 하얀색 테두리 추가 */
  border-radius: 12px;
  /* 모서리를 둥글게 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  /* 약간의 그림자 추가 */
  max-width: 400px;
  max-height: 500px;
}

.info-container h2 {
  margin-bottom: 1rem;
  margin-top: 1rem;
}

.info-container p {
  margin-bottom: 1.2rem;
}

.button-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.button-group button {
  padding: 12px 24px;
  font-size: 16px;
  font-weight: bold;
  color: white;
  background-color: #ffb703;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.button-group button:hover {
  background-color: #6a994e;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.button-group button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>
