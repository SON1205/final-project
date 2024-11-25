<template>
  <div class="main-content">
    <div class="modal-content">
      <h2>Upload Your Photo for Location {{ currentStep }} of {{ totalSteps }}</h2>

      <!-- 현재 단계의 SingleFileUpload 컴포넌트 렌더링 -->
      <SingleFileUpload :latitude="currentLocation.lat" :longitude="currentLocation.lng"
        :mapId="'street-view-' + currentStep" :uploadUrl="currentUploadUrl" />

      <!-- 이전/다음 버튼 -->
      <div class="step-buttons">
        <button @click="previousStep" :disabled="currentStep === 1">Previous</button>
        <button @click="nextStep" :disabled="currentStep === totalSteps">Next</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import SingleFileUpload from "@/components/SingleFileUpload.vue";

// 위도, 경도 데이터 배열
const locations = ref([
  { lat: 37.577462, lng: 126.991932 },
  { lat: 37.569737, lng: 126.995222 },
  { lat: 37.569348, lng: 126.999860 },
]);

// 업로드 URL 리스트
const uploadUrls = ref([
  "/api/upload/file1",
  "/api/upload/file2",
  "/api/upload/file3",
]);

// 현재 단계 및 총 단계
const currentStep = ref(1);
const totalSteps = computed(() => locations.value.length);

// 현재 단계에 따른 데이터 계산
const currentLocation = computed(() => locations.value[currentStep.value - 1]);
const currentUploadUrl = computed(() => uploadUrls.value[currentStep.value - 1]);
console.log(currentLocation);

// 단계 전환 함수
const nextStep = () => {
  if (currentStep.value < totalSteps.value) currentStep.value++;
};

const previousStep = () => {
  if (currentStep.value > 1) currentStep.value--;
};
</script>

<style scoped>
.main-content {
  position: relative;
  width: 100vw;
  height: 104vh;
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
  width: 100%;
  max-width: 1400px;
  min-height: 500px;
  padding: 20px;
  position: relative;
  text-align: center;
}

.modal-content h2 {
  margin-bottom: 20px;
}

.upload-list {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 30px;
}
</style>
