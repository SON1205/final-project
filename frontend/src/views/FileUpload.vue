<template>
  <div class="main-content">
    <div class="modal-content">
      <h2>Visit authentication</h2>
      <SingleFileUpload :latitude="currentLocation.lat" :longitude="currentLocation.lng"
        :mapId="'street-view-' + currentStep" :uploadUrl="currentUploadUrl" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import SingleFileUpload from "@/components/SingleFileUpload.vue";
import { useRouter } from "vue-router";
import { useFormStore } from "@/stores/form";

const router = useRouter();
const store = useFormStore();

// 위도, 경도 데이터 배열
const locations = ref([
  { lat: store.centerLatitude, lng: store.centerLongitude }
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
</script>

<style scoped>
#navigate-to-review-button button {
  margin-top: 2rem;
  display: inline-block;
  padding: 12px 24px;
  font-size: 13px;
  font-weight: bold;
  color: white;
  background-color: #ffb703;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

#navigate-to-review-button button:hover {
  background-color: #6a994e;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

#navigate-to-review-button button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
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
  background: rgb(22, 22, 22);
  border: 0.0625rem solid rgba(128, 128, 128, 0.4);
  border-radius: 0.5rem;
  box-shadow: rgba(0, 0, 0, 0.8) 0px 0.25rem 0.5rem 0px;
  color: rgba(255, 255, 255, 0.7);
  width: 100%;
  max-width: 440px;
  min-height: 500px;
  padding: 20px;
  position: relative;
  text-align: center;
}

.modal-content h2 {
  margin: 20px;
}

.upload-list {
  display: flex;
  flex-direction: row;
  justify-content: center;
  gap: 30px;
}
</style>
