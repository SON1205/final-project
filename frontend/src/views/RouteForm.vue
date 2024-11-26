<template>
  <div class="main-content">
    <div class="modal-content">
      <div class="content">
        <div class="question-header">
          <div class="district-selector" v-if="currentStep === 1">
            <p>Aren't you in {{ district }} right now?<br>
              If not, change the location</p>
            <select v-model="selectedDistrict" @change="handleDistrictChange">
              <option v-for="option in districtOptions" :key="option" :value="option">
                {{ option }}
              </option>
            </select>
          </div>
          <div v-if="currentStep === 2">
          </div>
          <div class="weather">
            <span class="temperature">{{ sky }} {{ tmp }}°C</span>
            <span class="precipitation">{{ pty }} {{ pop }}%</span>
          </div>
        </div>
        <div class="question-title">
          <h1>How would you like to take a stroll today?</h1>
        </div>
        <div>
          <!-- 1단계 -->
          <div v-if="currentStep === 1">
            <Step1Form @next="handleStep1" :options="step1Options" />
          </div>

          <!-- 2단계 -->
          <div v-else>
            <Step2Form @submit="handleStep2" :options="step2Options" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useFormStore } from '../stores/form';
import { ref, onMounted, computed } from "vue";
import axios from "axios";
import Step1Form from "@/components/Step1Form.vue";
import Step2Form from "@/components/Step2Form.vue";

const latitude = ref(0);
const longitude = ref(0);
const city = ref('정보 없음');
const district = ref("정보 없음");
const selectedDistrict = ref("정보 없음");

const store = useFormStore();
const districtOptions = store.districtOptions;

// 현재 위치
navigator.geolocation.getCurrentPosition(success, error);

function success(pos) {
  latitude.value = pos.coords.latitude;
  longitude.value = pos.coords.longitude;
  const url = `https://maps.googleapis.com/maps/api/geocode/json?latlng=${latitude.value},${longitude.value}&key=AIzaSyAJOR-gmnvhrB6vYHz3NQWQv8DcTreCqEo`;

  fetch(url)
    .then((response) => response.json())
    .then((data) => {
      if (data.status === "OK") {
        const addressComponents = data.results[0].address_components;
        district.value = addressComponents[2].long_name || "정보 없음";
        selectedDistrict.value = district.value;
        store.setDistrict(district.value);
        loadWeatherData(); // 위치 기반 날씨 데이터 로드
      } else {
        console.error("역 지오코딩 실패:", data.status);
      }
    })
    .catch((err) => console.error("API 요청 오류", err));
}

function error(err) {
  console.warn(`ERROR(${err.code}): ${err.message}`);
  city.value = "위치 정보를 가져올 수 없습니다.";
  district.value = "위치 정보를 가져올 수 없습니다.";
}

// 상태 관리
const currentStep = ref(1);
const step1Options = ref([]);
const step2Options = ref([]);
const selectedStep1 = ref(null);
const selectedStep2 = ref(null);

// 날씨 데이터 상태
const tmp = ref(null); // 온도
const sky = ref(null); // 하늘 상태
const pop = ref(null); // 강수 확률
const pty = ref(null);

// 선택된 지역의 grid 정보
const districtGrid = computed(() => store.districtGrid);

// 2단계 옵션 로드
async function handleStep1(selectedOption) {
  selectedStep1.value = selectedOption;
  currentStep.value = 2;
}

// 최종 경로 정보 로드
async function handleStep2(selectedOption) {
  selectedStep2.value = selectedOption; // 로컬 상태 저장
  store.setStep2(selectedOption); // Pinia 스토어 저장
}

// 지역 변경 이벤트 핸들러
function handleDistrictChange() {
  store.setDistrict(selectedDistrict.value);
  console.log(store.selectedDistrict); // Pinia 스토어에 저장
  loadWeatherData(); // 지역 변경 시 날씨 데이터 다시 로드
}

// 날씨 데이터 로드
async function loadWeatherData() {
  if (!districtGrid.value) {
    console.error("유효한 지역이 선택되지 않았습니다.");
    return;
  }

  const today = new Date();
  const year = today.getFullYear();
  const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
  const day = String(today.getDate()).padStart(2, '0'); // 1~9일도 두 자리로 변환

  const baseDate = `${year}${month}${day}`; // YYYYMMDD 형식

  try {
    const [nx, ny] = districtGrid.value; // Grid 좌표 가져오기
    const response = await axios.get("/api/weather/forecast", {
      params: {
        baseDate, // 기준 날짜
        baseTime: "0800", // 기준 시간 0200, 0500, 0800, 1100, 1400, 1700, 2000, 2300
        nx, // x 좌표
        ny, // y 좌표
      },
    });

    console.log(nx);
    console.log(ny);

    const items = response.data.response.body.items.item;

    items.forEach((item) => {
      switch (item.category) {
        case "TMP": // 기온
          tmp.value = item.fcstValue;
          break;

        case "SKY": // 하늘 상태
          sky.value = item.fcstValue === "1" ? "☀️" : item.fcstValue === "3" ? "⛅" : "☁️";
          break;

        case "PTY": // 강수 형태
          switch (item.fcstValue) {
            case "0":
              pty.value = " ";
              break;
            case "1":
              pty.value = "🌧️";
              break;
            case "2":
              pty.value = "🌧️/🌨️";
              break;
            case "3":
              pty.value = "🌨️";
              break;
            case "4":
              pty.value = "🌦️";
              break;
          }
          break;

        case "POP": // 강수 확률
          pop.value = item.fcstValue;
          break;
      }
    });
  } catch (error) {
    console.error("Failed to load weather data:", error);
  }
}

// 초기화
onMounted(() => {
  if (store.selectedDistrict) {
    loadWeatherData(); // 초기 선택된 구의 날씨 데이터 로드
  }
});
</script>

<style scoped>
/* 드롭다운 컨테이너 */
.district-selector {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

/* 드롭다운 선택 박스 */
select {
  text-align: center;
  appearance: none;
  /* 기본 브라우저 스타일 제거 */
  background-color: #2c2c2c;
  color: #ffffff;
  border: 1px solid #555555;
  border-radius: 8px;
  padding: 10px 15px;
  font-size: 15px;
  width: 90%;
  max-width: 300px;
  outline: none;
  cursor: pointer;
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

/* 선택 박스 Hover 및 Focus 효과 */
select:hover {
  border-color: #888888;
}

select:focus {
  border-color: #888888;
  box-shadow: 0 0 4px rgba(255, 255, 255, 0.5);
}

/* 드롭다운 옵션 */
option {
  background-color: #2c2c2c;
  color: #ffffff;
  padding: 10px;
  font-size: 16px;
}

/* 드롭다운 옵션 Hover 효과 */
select option:hover {
  background-color: #444444;
}

/* 알림 텍스트 */
.district-selector p {
  font-size: 14px;
  color: #cccccc;
  margin-bottom: 8px;
}

/* 메인 레이아웃 */
.main-content {
  position: relative;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 모달 */
.modal-content {
  background: rgba(33, 33, 33, 0.95);
  border: 1px solid rgba(128, 128, 128, 0.4);
  border-radius: 1rem;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.8);
  width: 90%;
  max-width: 800px;
  max-height: 470px;
  padding: 20px;
  text-align: center;
  animation: fadeIn 0.3s ease-out;
  color: rgba(255, 255, 255, 0.85);
}

/* 헤더 섹션 */
.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* margin-bottom: 10px; */
}

.district-selector,
.weather {
  font-size: 14px;
  font-weight: bold;
  color: rgba(255, 255, 255, 0.85);
}

.question-header {
  display: flex;
  justify-content: space-between;
  /* 양쪽 끝으로 배치 */
  align-items: center;
  /* 세로 정렬 */
  width: 100%;
}

.weather {
  flex-shrink: 0;
  /* 크기 고정 */
  text-align: right;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 10px;
  padding: 10px 20px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
}

/* .weather {
  text-align: left;
} */

.weather {
  display: flex;
  flex-direction: column;
  /* 아이템을 세로로 정렬 */
  align-items: center;
  /* 왼쪽 정렬 */
  font-size: 16px;
  font-weight: bold;
  color: rgba(255, 255, 255, 0.9);
  /* 텍스트 강조 */
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  border-radius: 10px;
  /* 둥근 테두리 */
  padding: 15px;
  /* 적당한 여백 */
  margin: 2px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
  /* 그림자 효과 */
}

.weather .temperature {
  font-size: 20px;
  /* 온도를 강조 */
  margin-bottom: 5px;
  /* 아래 간격 */
}

.weather .precipitation {
  font-size: 14px;
  /* 강수량은 상대적으로 작게 */
  color: rgba(255, 255, 255, 0.7);
  /* 약간 덜 강조 */
}

.weather::before {
  font-size: 20px;
  margin-bottom: 5px;
  display: block;
}


.question-title h1 {
  font-size: 1.8rem;
  margin-bottom: 1rem;
  color: #ffffff;
}

/* 애니메이션 */
@keyframes fadeIn {
  0% {
    opacity: 0;
    transform: scale(0.9);
  }

  100% {
    opacity: 1;
    transform: scale(1);
  }
}
</style>
