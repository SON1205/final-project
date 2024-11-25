<template>
  <div class="single-file-upload">
    <!-- Google Maps 로드뷰 -->
    <div class="map-container">
      <div :id="mapId" class="street-view"></div>
    </div>
    <!-- 파일 업로드 -->
    <div class="file-upload">
      <label for="file-input">
        <img :src="previewUrl || placeholderImage" alt="Upload Preview" class="upload-preview" />
      </label>
      <input id="file-input" type="file" @change="handleFileChange" accept="image/*" />
      <button @click="uploadFile">Upload</button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

const props = defineProps({
  latitude: {
    type: Number,
    required: true,
  },
  longitude: {
    type: Number,
    required: true,
  },
  mapId: {
    type: String,
    required: true, // 고유한 로드뷰 ID
  },
  uploadUrl: {
    type: String,
    required: true, // 업로드 URL
  },
});

const previewUrl = ref(null); // 파일 미리보기 URL
const file = ref(null); // 선택한 파일
const placeholderImage = "/assets/placeholder.png"; // 기본 이미지

// Google Maps Street View 초기화
function initStreetView() {
  console.log("Initializing Street View for:", props.latitude, props.longitude);

  const panorama = new google.maps.StreetViewPanorama(
    document.getElementById(props.mapId), // 고유 ID로 Street View 연결
    {
      position: { lat: props.latitude, lng: props.longitude },
      pov: { heading: 34, pitch: 10 },
      zoom: 1,
    }
  );

  mapId.setStreetView(panorama);
}

onMounted(() => {
  // Google Maps API 로드 확인
  if (window.google && window.google.maps) {
    initStreetView(); // 이미 로드된 경우 바로 초기화
  } else {
    const script = document.createElement("script");
    script.src = `https://maps.googleapis.com/maps/api/js?key=${import.meta.env.VITE_GOOGLE_MAPS_API_KEY}`;
    script.async = true;
    script.onload = initStreetView; // 스크립트 로드 후 초기화
    document.head.appendChild(script);
  }
});

// 파일 선택 처리
function handleFileChange(event) {
  file.value = event.target.files[0];
  if (file.value) {
    previewUrl.value = URL.createObjectURL(file.value);
  }
}

// 파일 업로드 처리
function uploadFile() {
  if (!file.value) {
    alert("Please select a file first.");
    return;
  }

  const formData = new FormData();
  formData.append("file", file.value);

  fetch(props.uploadUrl, {
    method: "POST",
    body: formData,
  })
    .then((response) => response.json())
    .then(() => {
      alert("File uploaded successfully!");
    })
    .catch(() => {
      alert("File upload failed.");
    });
}
</script>

<style scoped>
.single-file-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.map-container {
  width: 100%;
  height: 200px;
  border: 1px solid #ddd;
  border-radius: 10px;
  overflow: hidden;
}

.street-view {
  width: 400px;
  height: 200px;
}

.file-upload {
  display: flex;
  flex-direction: row;
  align-items: center;
}

input[type="file"] {
  display: none;
}

label {
  cursor: pointer;
}

.upload-preview {
  width: 300px;
  height: 150px;
  object-fit: cover;
  border: 2px dashed #aaa;
  border-radius: 10px;
  padding: 10px;
  transition: border-color 0.3s;
}

.upload-preview:hover {
  border-color: #45a049;
}

button {
  margin-left: 1.5rem;
  display: inline-block;
  padding: 10px 15px;
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

button:hover {
  background-color: #45a049;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

#select-route-button button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}
</style>
