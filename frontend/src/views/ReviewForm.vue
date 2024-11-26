<template>
  <div class="main-content">
    <div class="modal-content">
      <div class="review-container">
        <h2>Create Review</h2>
        <div class="img">
          <img :src="imageUrl" alt="Uploaded Image" />
        </div>
        <div class="rating">
          <label for="rating">Rate: </label>
          <button class="bookmark-btn" @click="toggleRating(n)" v-for="n in 5" :key="n">
            <font-awesome-icon :icon="n <= rating ? 'fas fa-star' : 'far fa-star'" />
          </button>
        </div>
        <button class="submit-button" @click="submitReview">Submit</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import axios from 'axios';
import { useAuthStore } from "@/stores/auth";
import { useFormStore } from "@/stores/form";
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const formStore = useFormStore();
const userId = authStore.userId;
const routeId = formStore.selectedRoute;
const router = useRouter();
console.log(routeId);

const rating = ref(1);
const imageUrl = ref(null); // 이미지 URL을 저장할 변수 선언

const toggleRating = (value) => {
  rating.value = value;
};

const computedRating = computed(() => rating.value);

const getImage = async () => {
  if (routeId && userId) {
    try {
      const response = await axios.get('/api/files/download', {
        params: {
          userId,
          routeId,
        },
        responseType: 'blob', // 바이너리 데이터로 가져옴
      });
      const blob = new Blob([response.data], { type: response.headers['content-type'] });
      imageUrl.value = URL.createObjectURL(blob); // imageUrl에 Blob을 URL로 변환해서 할당
    } catch (error) {
      console.error('이미지 가져오는 중 오류 발생:', error);
      alert('이미지를 가져오는 데 실패했습니다.');
    }
  } else {
    alert('유효한 userId와 routeId가 필요합니다.');
  }
};

onMounted(() => {
  getImage(); // 페이지 로드 시 이미지 자동 가져오기
});

const submitReview = async () => {
  if (rating.value) {
    // 서버에 전송할 리뷰 데이터
    const reviewDto = {
      userId, // 실제 사용자 ID
      routeId, // 실제 경로 ID
      regDate: new Date().toISOString(), // 현재 시간을 ISO 문자열로 전송
      rating: rating.value,
    };

    // POST 요청을 사용해 서버에 리뷰 데이터 전송
    try {
      const response = await axios.post('/api/reviews', reviewDto);
      if (response.status === 201) {
        alert('Your review has been submitted successfully.');
        router.push("/home");
      }
    } catch (error) {
      console.error('리뷰 제출 중 오류 발생:', error);
      alert('리뷰 제출에 실패했습니다.');
    }
  } else {
    alert('Please select your rating.');
  }
};
</script>

<style scoped>
.img img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 0.5rem;
}

.img {
  width: 100%;
  max-width: 200px;
  height: auto;
  background-color: #ccc;
  border: 0.0625rem solid rgba(128, 128, 128, 0.4);
  border-radius: 0.5rem;
  box-shadow: rgba(0, 0, 0, 0.8) 0px 0.25rem 0.5rem;
  overflow: hidden;
  margin-bottom: 20px;
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

.review-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  /* max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); */
}

h2 {
  text-align: center;
  margin-bottom: 30px;
}

.rating {
  margin: 15px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

label {
  margin-right: 10px;
}

.submit-button {
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

.submit-button:hover {
  background-color: #6a994e;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

.submit-button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.bookmark-btn {
  background: transparent;
  border: none;
  cursor: pointer;
  font-size: 15px;
  color: #ffb703;
  transition: color 0.3s ease;
  margin-right: 5px;
  margin-left: 5px;
}

.bookmark-btn:hover {
  color: #f48c06;
}
</style>
